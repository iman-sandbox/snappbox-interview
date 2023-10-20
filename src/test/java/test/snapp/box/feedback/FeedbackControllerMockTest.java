package test.snapp.box.feedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import test.snapp.box.feedback.actor.feedback.FeedbackController;
import test.snapp.box.feedback.actor.feedback.model.FeedbackSubmitRequest;
import test.snapp.box.feedback.application.advice.GlobalExceptionHandler;
import test.snapp.box.feedback.application.advice.exceptions.AlreadyExistException;
import test.snapp.box.feedback.application.advice.exceptions.ForbiddenResourceException;
import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import test.snapp.box.feedback.domain.customer.CustomerService;
import test.snapp.box.feedback.domain.customer.data.Customer;
import test.snapp.box.feedback.domain.feedback.FeedbackService;
import test.snapp.box.feedback.domain.feedback.data.Feedback;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FeedbackController.class)
@ContextConfiguration(classes = {FeedbackController.class, TestSecurityConfig.class, GlobalExceptionHandler.class})
@WithMockUser(username = "test", roles = {"CUSTOMER"})
public class FeedbackControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testSubmitFeedbackSuccess() throws Exception {
        FeedbackSubmitRequest request = new FeedbackSubmitRequest();
        request.setDeliveryId(1L);
        request.setRating(5);
        request.setComments("Great service!");

        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setDeliveryId(1L);
        feedback.setRating(5);
        feedback.setComments("Great service!");

        when(feedbackService.submitFeedback(anyLong(), any(FeedbackSubmitRequest.class))).thenReturn(feedback);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUserId(1L);
        customer.setName("Iman");
        when(customerService.getByBearerToken(anyString())).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/feedback").with(user("test")).header("Authorization", "Bearer mockToken").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isCreated());

        verify(customerService).getByBearerToken(anyString());
        verify(feedbackService).submitFeedback(any(), any());
    }


    @Test
    public void testSubmitFeedbackInvalidRating() throws Exception {
        String feedbackJson = "{ \"deliveryId\": 1, \"rating\": 6, \"comments\": \"Rating too high!\" }";

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUserId(1L);
        customer.setName("Iman");
        when(customerService.getByBearerToken(anyString())).thenReturn(customer);

        mockMvc.perform(post("/api/feedback").with(user("test")).header("Authorization", "Bearer mockToken").contentType(MediaType.APPLICATION_JSON).content(feedbackJson)).andExpect(status().isBadRequest()).andDo(h -> System.out.println(h.getResponse().getContentAsString()));
    }

    @Test
    public void testSubmitFeedbackNoDeliveryForCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUserId(1L);
        customer.setName("Iman");
        when(customerService.getByBearerToken(anyString())).thenReturn(customer);

        doThrow(new ForbiddenResourceException("Delivery", 1L)).when(feedbackService).submitFeedback(anyLong(), any(FeedbackSubmitRequest.class));

        String feedbackJson = "{ \"deliveryId\": 1, \"rating\": 5, \"comments\": \"Great delivery!\" }";

        mockMvc.perform(post("/api/feedback").with(user("test")).header("Authorization", "Bearer mockToken").contentType(MediaType.APPLICATION_JSON).content(feedbackJson)).andDo(h -> System.out.println(h.getResponse().getContentAsString())).andExpect(status().isForbidden());
    }

    @Test
    public void testSubmitFeedbackCustomerNotFound() throws Exception {
        when(customerService.getByBearerToken(anyString())).thenThrow(new NotFoundException("Customer"));

        String feedbackJson = "{ \"deliveryId\": 1, \"rating\": 5, \"comments\": \"Great delivery!\" }";

        mockMvc.perform(post("/api/feedback").with(user("test")).header("Authorization", "Bearer mockToken").contentType(MediaType.APPLICATION_JSON).content(feedbackJson)).andDo(h -> System.out.println(h.getResponse().getContentAsString())).andExpect(status().isNotFound());
    }

    @Test
    public void testSubmitFeedbackAlreadyExists() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUserId(1L);
        customer.setName("Iman");
        when(customerService.getByBearerToken(anyString())).thenReturn(customer);

        String feedbackJson = "{ \"deliveryId\": 1, \"rating\": 5, \"comments\": \"Great delivery!\" }";

        doThrow(new AlreadyExistException("Feedback")).when(feedbackService).submitFeedback(anyLong(), any(FeedbackSubmitRequest.class));

        mockMvc.perform(post("/api/feedback").with(user("test")).header("Authorization", "Bearer mockToken").contentType(MediaType.APPLICATION_JSON).content(feedbackJson)).andExpect(status().isConflict()).andDo(h -> System.out.println(h.getResponse().getContentAsString()));

    }

}
