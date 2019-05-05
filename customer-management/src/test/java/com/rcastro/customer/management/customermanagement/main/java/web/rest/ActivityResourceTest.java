package com.rcastro.customer.management.customermanagement.main.java.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcastro.customer.management.customermanagement.CustomerManagementApplication;
import com.rcastro.customer.management.customermanagement.domain.Activity;
import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.service.ActivityService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.web.rest.ActivityResource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ActivityResource.class)
@ContextConfiguration(classes = CustomerManagementApplication.class)
public class ActivityResourceTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ActivityService activityService;

    @MockBean
    protected UserService userService;


    @Test
    public void shouldReturnFoundActivities() throws Exception {

        // given
        User user = createUser("Niko", LocalDate.now());
        Activity activity = createActivity(user, "Test");
        List<Activity> activities = new ArrayList<>();
        activities.add(activity);

        // when
        when(userService.findUser(1l)).thenReturn(Optional.of(user));
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "_id"));
        when(activityService.findActivities(user,pageable)).thenReturn(activities);

        // then
        String contentAsString = mockMvc.perform(get("/api/activity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("user-id",1l)
                .param("size", "10")
                .param("page", "0")
                .param("sort", "DESC")
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(contentAsString).isEqualTo("[{\"user\":{\"id\":1,\"name\":\"Niko\",\"birthday\":\"2019-05-05\",\"created\":null,\"wallet\":null},\"description\":\"Test\",\"created\":\"2019-05-06T00:00:00\"}]");

    }

    private Activity createActivity(User user, String description) {
        return new Activity(user, description, LocalDateTime.of(2019,05,06,0,0,0));
    }


    private User createUser(String name, LocalDate birthday) {
        User user = new User();
        user.setName(name);
        user.setId(1l);
        user.setBirthday(birthday);
        return user;
    }
}
