package com.google.alt_runtime_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AltRuntimeDemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testEntrypoint() throws Exception {
        mockMvc.perform(get("/test")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void homeEntrypoint() throws Exception {
        mockMvc.perform(post("/home").contentType(APPLICATION_JSON).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            action: {
                                navigations: [{
                                    pushCard: {
                                          "header": {
                                            "title": "Sample Addon on Alt runtime",
                                            "imageUrl": "https://developers.google.com/chat/images/quickstart-app-avatar.png",
                                            "imageType": "CIRCLE"
                                          },
                                          "sections": [
                                            {
                                              "header": "How to test?",
                                              "widgets": [
                                                {
                                                  "textParagraph": {
                                                    "text": "Select an event by clicking on Calendar Grid."
                                                  }
                                                }
                                              ]
                                            }
                                          ]
                                    }
                                }]
                            }
                        }
                        """));
    }

    @Test
    void calendarEventOpenTrigger() throws Exception {
        mockMvc.perform(post("/eventOpenTrigger").contentType(APPLICATION_JSON).content("""
                        {
                            "calendar": {
                                "calendarId": "test@google.com",
                                "id": "asdfqwersa",
                                "organizer": {"email": "test@google.com"}
                            }
                        }
                        """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            action: {
                                navigations: [{
                                    pushCard: {
                                          "header": {
                                            "title": "Sample Addon on Alt runtime",
                                            "imageUrl": "https://developers.google.com/chat/images/quickstart-app-avatar.png",
                                            "imageType": "CIRCLE"
                                          },
                                          "sections": [
                                            {
                                              "header": "Selected event",
                                              "widgets": [
                                                {
                                                  "textParagraph": {
                                                    "text": "Calendar: test@google.com"
                                                  }
                                                },
                                                {
                                                  "textParagraph": {
                                                    "text": "Event id: asdfqwersa"
                                                  }
                                                },
                                                {
                                                  "textParagraph": {
                                                    "text": "Organizer id: test@google.com"
                                                  }
                                                }
                                              ]
                                            },
                                            {
                                              "header": "Event manipulations",
                                              "widgets": [
                                                {
                                                  "buttonList": {
                                                    "buttons": [
                                                      {
                                                        "text": "Attach conference data",
                                                        "onClick": {
                                                            "action": {
                                                                "function": "https://test-650670410778.europe-west2.run.app/attachConference"
                                                            }
                                                        }
                                                      }
                                                    ]
                                                  }
                                                }
                                              ]
                                            },
                                            {
                                              "header": "Debugging data",
                                              "widgets": [
                                                {
                                                  "textInput": {
                                                    "name": "Input",
                                                    "label": "Input data",
                                                    "value": "true",
                                                    "type": MULTIPLE_LINE
                                                  }
                                                }
                                              ]
                                            }
                                          ]
                                    }
                                }]
                            }
                        }
                        """));
    }
}
