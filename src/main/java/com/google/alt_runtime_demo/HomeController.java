package com.google.alt_runtime_demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @RequestMapping(value = "/test")
    public @ResponseBody String test() {
        return "asdf";
    }

    @PostMapping("/home")
    @ResponseBody
    public String home(@RequestBody JsonNode event) {
        CardItem pushCard = new CardItem()
                .setProperty("header",
                        Map.of("title", "Sample Addon on Alt runtime",
                                "imageUrl", "https://developers.google.com/chat/images/quickstart-app-avatar.png",
                                "imageType", "CIRCLE"
                        ))
                .setSingleElementArrayProperty("sections",
                        new CardSection("How to test?")
                                .addWidget(new CardTextParagraph("Select an event by clicking on Calendar Grid.")));
        return new CardItem().setProperty("action",
                new CardItem().setSingleElementArrayProperty("navigations",
                        new CardItem().setProperty("pushCard", pushCard)
                )).render();
    }

    @PostMapping("/eventOpenTrigger")
    @ResponseBody
    public String calendarEventOpenTrigger(@RequestBody JsonNode event) {
        String calendarId = event.at("/calendar/calendarId").asText();
        String eventId = event.at("/calendar/id").asText();
        String organizerId = event.at("/calendar/organizer/email").asText();
        boolean canSetConfData = event.at("/calendar/capabilities/canSetConferenceData").asBoolean();

        CardItem pushCard = new CardItem()
                .setProperty("header",
                        Map.of("title", "Sample Addon on Alt runtime",
                                "imageUrl", "https://developers.google.com/chat/images/quickstart-app-avatar.png",
                                "imageType", "CIRCLE"
                        ))
                .setProperty("sections", new CardItemArray()
                        .add(new CardSection("Selected event")
                                .addWidget(new CardTextParagraph("Calendar: " + calendarId))
                                .addWidget(new CardTextParagraph("Event id: " + eventId))
                                .addWidget(new CardTextParagraph("Organizer id: " + organizerId)))
                        .add(new CardSection("Event manipulations")
                                .addWidget(new CardItem().setProperty("buttonList",
                                        new CardItem().setProperty("buttons",
                                                new CardItemArray().add(
                                                        new CardItem()
                                                                .setProperty("text", "Attach conference data")
                                                                .setProperty("disabled", String.valueOf(!canSetConfData))
                                                                .setProperty("onClick",
                                                                        new CardItem().setProperty("action",
                                                                                Map.of(
                                                                                        "function", "https://test-650670410778.europe-west2.run.app/attachConference"))
                                                                )))
                                )))
                        .add(new CardSection("Debugging data")
                                .addWidget(
                                        new CardItem().setProperty("textInput",
                                                Map.of("name", "Input",
                                                        "label", "Input data",
                                                        "value", event.toPrettyString(),
                                                        "type", "MULTIPLE_LINE"
                                                ))))
                );

        return new CardItem().setProperty("action",
                new CardItem().setSingleElementArrayProperty("navigations",
                        new CardItem().setProperty("pushCard", pushCard)
                )).render();
    }

    @PostMapping("/attachConference")
    @ResponseBody
    public String attachConference(@RequestBody JsonNode event) {
        CardItem calendarAction = new CardItem().setProperty("editConferenceDataActionMarkup",
                new CardItem().setProperty("setConferenceData",
                        new CardItem()
                                .setProperty("conferenceSolutionId", "conf_sol_123")
                                .setProperty("entryPoint",
                                        new CardItemArray()
                                                .add(new CardItem().setProperty("type", "VIDEO")
                                                        .setProperty("uri", "http://meet.google.com/asd-qwer-zxc")
                                                        .setProperty("meetingCode", "asd-qwer-zxc"))
                                )
                ));
        return new CardItem().setProperty("renderActions",
                        new CardItem().setProperty("hostAppAction",
                                new CardItem().setProperty("calendarAction", calendarAction)))
                .render();
    }


    interface CardElementI {
        JsonElement getElement();
    }

    class CardItem implements CardElementI {
        private JsonObject jsonObject = new JsonObject();

        @Override
        public JsonElement getElement() {
            return jsonObject;
        }

        public CardItem setProperty(String name, String value) {
            jsonObject.add(name, new JsonPrimitive(value));
            return this;
        }

        public CardItem setProperty(String name, Map<String, String> values) {
            JsonObject obj = new JsonObject();
            values.forEach((k, v) -> obj.add(k, new JsonPrimitive(v)));
            jsonObject.add(name, obj);
            return this;
        }

        public CardItem setProperty(String name, CardElementI value) {
            jsonObject.add(name, value.getElement());
            return this;
        }

        public CardItem setSingleElementArrayProperty(String name, CardElementI value) {
            CardItemArray cardArray = new CardItemArray();
            cardArray.add(value);

            return setProperty(name, cardArray);
        }

        public String render() {
            return gson.toJson(jsonObject);
        }
    }

    class CardItemArray implements CardElementI {
        private com.google.gson.JsonArray jsonObject = new com.google.gson.JsonArray();

        @Override
        public JsonElement getElement() {
            return jsonObject;
        }

        public CardItemArray add(CardElementI cardElementI) {
            return add(cardElementI.getElement());
        }

        public CardItemArray add(JsonElement jsonElement) {
            jsonObject.add(jsonElement);
            return this;
        }
    }

    class CardSection implements CardElementI {
        private final String header;
        List<CardElementI> widgets = new ArrayList<>();

        CardSection(String header) {
            this.header = header;
        }

        public CardSection addWidget(CardElementI widget) {
            widgets.add(widget);
            return this;
        }

        @Override
        public JsonElement getElement() {
            JsonObject result = new JsonObject();
            result.add("header", new JsonPrimitive(header));
            if (widgets.size() > 0) {
                com.google.gson.JsonArray widgetArr = new com.google.gson.JsonArray();
                widgets.stream().map(CardElementI::getElement).forEach(widgetArr::add);
                result.add("widgets", widgetArr);
            }
            return result;
        }
    }

    class CardTextParagraph implements CardElementI {
        private final String text;

        private CardTextParagraph(String text) {
            this.text = text;
        }

        @Override
        public JsonElement getElement() {
            JsonObject textObject = new JsonObject();
            textObject.add("text", new JsonPrimitive(text));
            JsonObject component = new JsonObject();
            component.add("textParagraph", textObject);
            return component;
        }
    }
}
