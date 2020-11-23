package com.chaoip.ipproxy;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@SpringBootTest
@ActiveProfiles("unittest")
class ManageControllerTest extends BaseTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper mapper;

    @Test
    void contextLoads() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        getAllTest(mockMvc);

        addTest(mockMvc);
    }

    void getAllTest(MockMvc mockMvc) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/manage/routes");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andReturn().getResponse().setCharacterEncoding("UTF-8"); // 避免print乱码

        result.andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.content().string("1"))
                .andDo(MockMvcResultHandlers.print());
    }

    void addTest(MockMvc mockMvc) throws Exception {
        RouteDto dto = RouteDto.builder()
                .ip("12.34.33.22")
                .port(8901)
                .protocal("https")
                .area("福州")
                .expireTime(LocalDateTime.now().minusDays(-10))
                .build();
        String content = mapper.writeValueAsString(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/manage/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("a1", "b1")
                .param("a", "b");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andReturn().getResponse().setCharacterEncoding("UTF-8"); // 避免print乱码

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"))
                .andDo(MockMvcResultHandlers.print());
    }
}
