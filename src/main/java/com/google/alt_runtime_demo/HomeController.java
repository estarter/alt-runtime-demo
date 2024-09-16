package com.google.alt_runtime_demo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(@RequestBody(required = false) String json) {
        return "asdf";
    }

    @PostMapping("/home")
    @ResponseBody
    public String onViewMessage(@RequestBody JsonNode event) {
//        ObjectMapper response = new ObjectMapper();
//        ObjectNode responseNode = response.createObjectNode();
//        responseNode.put("sections", "asdf");
//        return responseNode;
        return """
                {
                    action: {
                        navigations: [{
                            updateCard: {
                                  "header": {
                                    "title": "Test card",
                                    "imageUrl": "https://developers.google.com/chat/images/quickstart-app-avatar.png",
                                    "imageType": "CIRCLE"
                                  },
                                  "sections": [
                                    {
                                      "header": "Section Header",
                                      "widgets": [
                                        {
                                          "textParagraph": {
                                            "text": "See <a href=https://developers.google.com/apps-script/add-ons/concepts/widgets#text_formatting>this doc</a> for rich text formatting"
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

    /*


     */
}
