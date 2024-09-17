package com.google.alt_runtime_demo;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping(value = "/test")
    public @ResponseBody String test() {
        return "asdf";
    }

    @PostMapping("/home")
    @ResponseBody
    public String home(@RequestBody JsonNode event) {
        return """
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
                """;
    }

    @PostMapping("/eventOpenTrigger")
    @ResponseBody
    public String calendarEventOpenTrigger(@RequestBody JsonNode event) {
        String calendarId = event.at("/calendar/calendarId").asText();
        String eventId = event.at("/calendar/id").asText();
        String organizerId = event.at("/calendar/organizer/email").asText();
        return """
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
                                            "text": "Calendar: %s"
                                          }
                                        },
                                        {
                                          "textParagraph": {
                                            "text": "Event id: %s"
                                          }
                                        },
                                        {
                                          "textParagraph": {
                                            "text": "Organizer id: %s"
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
                                            "value": "%s",
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
                """.formatted(calendarId, eventId, organizerId, calendarId.equals(organizerId), event.toPrettyString().replaceAll("\"", ""));
    }

    @PostMapping("/attachConference")
    @ResponseBody
    public String attachConference(@RequestBody JsonNode event) {
        return """
                {
                    "actionResponse": []
                }
                """;
    }
}
