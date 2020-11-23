package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.Increment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * AutoIncListener
 * 监听mongo的save事件，并设置自增id.
 * 通过mongo的一个Collection来收集自增数据
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/18 11:29
 */
@Component
@Slf4j
public class AutoIncListener extends AbstractMongoEventListener {
    private final MongoTemplate mongoTemplate;

    public AutoIncListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent event) {   // 不要用错为 onBeforeSave
        // super.onBeforeSave(event);

        Object entity = event.getSource();
        ReflectionUtils.doWithFields(entity.getClass(), field -> setIdInc(field, entity));
//        org.bson.Document document = BeforeSaveEvent.getDocument();
//        if (document != null) {
//            String id = String.valueOf(document.get("_id"));
//            if (id == null || id.equals("0")) {
//                document.put("_id", getNextId(entity.getClass().getSimpleName()));
//            }
//        }
    }

    private void setIdInc(Field field, Object source) throws IllegalAccessException {
        ReflectionUtils.makeAccessible(field);
        // 如果字段定义了Id注解，并且为空或0
        if (field.isAnnotationPresent(Id.class)) {
            Object val = field.get(source);
            if (val == null || val.toString().equals("0")) {
                // 设置自增ID
                field.set(source, getNextId(source.getClass().getSimpleName()));
            }
        }
    }

    private int getNextId(String collectionName) {
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);

        Update update = new Update();
        update.inc("currentId", 1);
        Query query = new Query(Criteria.where("collection").is(collectionName));
        Increment inc = mongoTemplate.findAndModify(query, update, options, Increment.class);
        assert inc != null;
        return inc.getCurrentId();
    }
}
