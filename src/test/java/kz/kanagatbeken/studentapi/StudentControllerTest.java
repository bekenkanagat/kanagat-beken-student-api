package kz.kanagatbeken.studentapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long createStudent(String email) throws Exception {
        String body = """
                {"firstName":"Kanagat","lastName":"Beken","email":"%s","faculty":"IT","course":3}
                """.formatted(email);
        MvcResult result = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Kanagat"))
                .andExpect(jsonPath("$.lastName").value("Beken"))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void createsAndReturnsStudentById() throws Exception {
        long id = createStudent("kanagat.create@example.com");

        mockMvc.perform(get("/api/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value("kanagat.create@example.com"));
    }

    @Test
    void returnsAllStudents() throws Exception {
        createStudent("kanagat.list@example.com");

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void updatesStudent() throws Exception {
        long id = createStudent("kanagat.update@example.com");
        String body = """
                {"firstName":"Kanagat","lastName":"Beken","email":"kanagat.update@example.com","faculty":"DevOps","course":4}
                """;

        mockMvc.perform(put("/api/students/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.faculty").value("DevOps"))
                .andExpect(jsonPath("$.course").value(4));
    }

    @Test
    void deletesStudent() throws Exception {
        long id = createStudent("kanagat.delete@example.com");

        mockMvc.perform(delete("/api/students/" + id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/students/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns404ForNonexistentStudent() throws Exception {
        mockMvc.perform(get("/api/students/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student with id 999999 not found"));
    }

    @Test
    void returns400ForInvalidStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"lastName\":\"Beken\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void infoEndpointShowsAuthor() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Wrong Name"));
    }
}
