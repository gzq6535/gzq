package cn.itcast;

import cn.itcast.pojo.Goods;
import cn.itcast.pojo.Item;
import cn.itcast.repositroy.GoodsRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SDEManager {

    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void testDoc(){
//        新增或修改
//        Goods goods = new Goods(1L,"小米6X手机","手机","小米",1199.0,"weehtgc");
//        goodsRepository.save(goods);

//        删除
//        goodsRepository.deleteById(1L);

        // 准备文档数据：
        List<Goods> list = new ArrayList<>();
        list.add(new Goods(1L, "小米手机7", "手机", "小米", 3299.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Goods(2L, "坚果手机R1", "手机", "锤子", 3699.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Goods(3L, "华为META10", "手机", "华为", 4499.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Goods(4L, "小米Mix2S", "手机", "小米", 4299.00,"http://image.leyou.com/13123.jpg"));
        list.add(new Goods(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));

        goodsRepository.saveAll(list); //批量新增

    }

    @Test
    public void testQuery(){
//        查询所有
       /* Iterable<Goods> goodsList = goodsRepository.findAll();
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }

//根据id查询
        Optional<Goods> optional = goodsRepository.findById(1L);
        Goods goods = optional.get();*/

//       分页查询
       /* Page<Goods> page = goodsRepository.findAll(PageRequest.of(0, 5)); //当前页码是从0开始
        List<Goods> goodsList1 = page.getContent();
        for (Goods goods1 : goodsList1) {
            System.out.println(goods1);
        }*/

//        List<Goods> goodsList1 =  goodsRepository.findByTitle("小米");
//        List<Goods> goodsList1 =  goodsRepository.findByPriceBetween(3000.0,5000.0);
//        List<Goods> goodsList1 = goodsRepository.findByTitleAndPriceBetween("小米",3000.0,5000.0);
        List<Goods> goodsList1 = goodsRepository.findByTitleOrPriceBetween("小米",3000.0,5000.0);
        for (Goods goods1 : goodsList1) {
            System.out.println(goods1);
        }
    }

    @Test
    public void createIndex(){
//        esTemplate.createIndex(Goods.class); //创建索引库
        esTemplate.putMapping(Goods.class); //创建映射

    }


    @Test
    public void nativeQuery(){
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(QueryBuilders.termQuery("title","小米"));


        AggregatedPage<Goods> aggregatedPage = esTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);

        List<Goods> goodsList = aggregatedPage.getContent();
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

}
