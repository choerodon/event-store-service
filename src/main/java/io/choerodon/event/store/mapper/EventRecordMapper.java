package io.choerodon.event.store.mapper;

import io.choerodon.event.store.domain.EventRecord;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhipeng.zuo on 2017/9/29.
 * @author zhipeng.zuo
 */
public interface EventRecordMapper extends BaseMapper<EventRecord> {

  /**
   * 查询所有未完成的事件
   * @return 未完成的事件的list
   */
  @Select ({
          "select * from event_record where is_finished = '0'"
  })
  @Results({
          @Result(column = "create_time", property = "createTime"),
          @Result(column = "is_finished", property = "isFinished"),
  })
  List<EventRecord> selectInitEvent();

}
