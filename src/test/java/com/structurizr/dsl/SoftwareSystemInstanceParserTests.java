package com.structurizr.dsl;

import com.structurizr.model.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SoftwareSystemInstanceParserTests extends AbstractTests {

    private SoftwareSystemInstanceParser parser = new SoftwareSystemInstanceParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("softwareSystemInstance", "identifier", "group", "tags", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: softwareSystemInstance <identifier> [deploymentGroup|tags] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheIdentifierIsNotSpecified() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("softwareSystemInstance"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: softwareSystemInstance <identifier> [deploymentGroup|tags] [tags]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        try {
            parser.parse(new DeploymentNodeDslContext(null), tokens("softwareSystemInstance", "softwareSystem"));
            fail();
        } catch (Exception e) {
            assertEquals("The software system \"softwareSystem\" does not exist", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementIsNotASoftwareSystem() {
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(null);
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", model.addPerson("Name", "Description"));
        context.setElements(elements);

        try {
            parser.parse(context, tokens("softwareSystemInstance", "softwareSystem"));
            fail();
        } catch (Exception e) {
            assertEquals("The element \"softwareSystem\" is not a software system", e.getMessage());
        }
    }

    @Test
    void test_parse_CreatesASoftwareSystemInstanceInTheDefaultDeploymentGroup() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        parser.parse(context, tokens("softwareSystemInstance", "softwareSystem"));

        assertEquals(3, model.getElements().size());
        assertEquals(1, deploymentNode.getSoftwareSystemInstances().size());
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.getSoftwareSystemInstances().iterator().next();
        assertSame(softwareSystem, softwareSystemInstance.getSoftwareSystem());
        assertEquals("Software System Instance", softwareSystemInstance.getTags());
        assertEquals("Live", softwareSystemInstance.getEnvironment());
        assertEquals("Default", softwareSystemInstance.getDeploymentGroup());
    }

    @Test
    void test_parse_CreatesASoftwareSystemInstanceInTheDefaultDeploymentGroupWithTags() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", softwareSystem);
        context.setElements(elements);

        parser.parse(context, tokens("softwareSystemInstance", "softwareSystem", "Tag 1, Tag 2"));

        assertEquals(3, model.getElements().size());
        assertEquals(1, deploymentNode.getSoftwareSystemInstances().size());
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.getSoftwareSystemInstances().iterator().next();
        assertSame(softwareSystem, softwareSystemInstance.getSoftwareSystem());
        assertEquals("Software System Instance,Tag 1,Tag 2", softwareSystemInstance.getTags());
        assertEquals("Live", softwareSystemInstance.getEnvironment());
        assertEquals("Default", softwareSystemInstance.getDeploymentGroup());
    }

    @Test
    void test_parse_CreatesASoftwareSystemInstanceInTheSpecifiedDeploymentGroup() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", softwareSystem);
        elements.put("group", new DeploymentGroup("Group"));
        context.setElements(elements);

        parser.parse(context, tokens("softwareSystemInstance", "softwareSystem", "group"));

        assertEquals(3, model.getElements().size());
        assertEquals(1, deploymentNode.getSoftwareSystemInstances().size());
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.getSoftwareSystemInstances().iterator().next();
        assertSame(softwareSystem, softwareSystemInstance.getSoftwareSystem());
        assertEquals("Software System Instance", softwareSystemInstance.getTags());
        assertEquals("Live", softwareSystemInstance.getEnvironment());
        assertEquals("Group", softwareSystemInstance.getDeploymentGroup());
    }

    @Test
    void test_parse_CreatesASoftwareSystemInstanceInTheSpecifiedDeploymentGroupWithTags() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        DeploymentNode deploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        DeploymentNodeDslContext context = new DeploymentNodeDslContext(deploymentNode);
        Map<String, Element> elements = new HashMap<>();
        elements.put("softwaresystem", softwareSystem);
        elements.put("group", new DeploymentGroup("Group"));
        context.setElements(elements);

        parser.parse(context, tokens("softwareSystemInstance", "softwareSystem", "group", "Tag 1, Tag 2"));

        assertEquals(3, model.getElements().size());
        assertEquals(1, deploymentNode.getSoftwareSystemInstances().size());
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.getSoftwareSystemInstances().iterator().next();
        assertSame(softwareSystem, softwareSystemInstance.getSoftwareSystem());
        assertEquals("Software System Instance,Tag 1,Tag 2", softwareSystemInstance.getTags());
        assertEquals("Live", softwareSystemInstance.getEnvironment());
        assertEquals("Group", softwareSystemInstance.getDeploymentGroup());
    }

}