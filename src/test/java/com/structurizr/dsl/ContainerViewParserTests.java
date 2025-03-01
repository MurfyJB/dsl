package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.view.ContainerView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContainerViewParserTests extends AbstractTests {

    private ContainerViewParser parser = new ContainerViewParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("container", "identifier", "key", "description", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: container <software system identifier> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSoftwareSystemIdentifierIsMissing() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("container"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: container <software system identifier> [key] [description] {", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheSoftwareSystemIsNotDefined() {
        DslContext context = context();
        try {
            parser.parse(context, tokens("container", "softwareSystem", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The software system \"softwareSystem\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotASoftwareSystem() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addPerson("Name", "Description"));
        context.setElements(elements);

        try {
            parser.parse(context, tokens("container", "softwareSystem", "key"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" is not a software system", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesAContainerView() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("container", "softwareSystem"));
        List<ContainerView> views = new ArrayList<>(context.getWorkspace().getViews().getContainerViews());

        assertEquals(1, views.size());
        assertEquals("Name-Container", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalSoftwareSystemBoundariesVisible());
    }

    @Test
    void test_parse_CreatesAContainerViewWithAKey() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("container", "softwareSystem", "key"));
        List<ContainerView> views = new ArrayList<>(context.getWorkspace().getViews().getContainerViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalSoftwareSystemBoundariesVisible());
    }

    @Test
    void test_parse_CreatesAContainerViewWithAKeyAndDescription() {
        DslContext context = context();
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addSoftwareSystem("Name", "Description"));
        context.setElements(elements);

        parser.parse(context, tokens("container", "softwareSystem", "key", "Description"));
        List<ContainerView> views = new ArrayList<>(context.getWorkspace().getViews().getContainerViews());

        assertEquals(1, views.size());
        assertEquals("key", views.get(0).getKey());
        assertEquals("Description", views.get(0).getDescription());
        assertTrue(views.get(0).getExternalSoftwareSystemBoundariesVisible());
    }

}