package com.griffin.aicodemother.service;

import com.griffin.aicodemother.model.dto.app.AppQueryRequest;
import com.griffin.aicodemother.model.entity.App;
import com.griffin.aicodemother.model.entity.User;
import com.griffin.aicodemother.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author griffin
 */
public interface AppService extends IService<App> {

    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);

    Flux<String> chatToGenCode(Long appId, String userMessage, User loginUser);

    String deployApp(Long appId,User loginUser);

}
