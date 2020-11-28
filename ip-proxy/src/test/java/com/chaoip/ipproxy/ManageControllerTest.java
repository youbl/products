package com.chaoip.ipproxy;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.RouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ActiveProfiles("unittest")
class ManageControllerTest extends BaseTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RouteRepository routeRepository;

    @Test
    void contextLoads() throws Exception {
        // 数据清理
        routeRepository.deleteAll();
        Assert.assertTrue(routeRepository.findAll().size() == 0);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        addTest(mockMvc);
        getAllTest(mockMvc);
    }

    void getAllTest(MockMvc mockMvc) throws Exception {
        callAll(mockMvc, 4);
        Thread.sleep(60000);
        callAll(mockMvc, 2);
        Thread.sleep(60000);
        callAll(mockMvc, 1);
    }

    void callAll(MockMvc mockMvc, int num) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/manage/routes");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andReturn().getResponse().setCharacterEncoding("UTF-8"); // 避免print乱码

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(num)))
                .andDo(MockMvcResultHandlers.print());

    }

    void addTest(MockMvc mockMvc) throws Exception {
        RouteDto dto = RouteDto.builder()
                .ip("12.34.33.22")
                .port(8901)
                .protocal("https")
                .area("0591")
                .operators("mobile")
                .expireTime(60 * 60 * 24 * 30)
                .build();
        callAdd(dto, mockMvc); // 失效数据

        dto.setExpireTime(1);
        callAdd(dto, mockMvc); // 失效数据

        dto.setExpireTime(-61);
        callAdd(dto, mockMvc); // 一分钟后失效

        dto.setExpireTime(-3600);
        callAdd(dto, mockMvc); // 明天失效
    }

    void callAdd(RouteDto dto, MockMvc mockMvc) throws Exception {

        String content = mapper.writeValueAsString(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/manage/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andReturn().getResponse().setCharacterEncoding("UTF-8"); // 避免print乱码

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));
        //.andDo(MockMvcResultHandlers.print());
    }
}
