package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.RelationshipView;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeploymentViewContentParserTests extends AbstractTests {

    private DeploymentViewContentParser parser = new DeploymentViewContentParser();

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        try {
            parser.parseInclude(new DeploymentViewDslContext(null), tokens("include"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: include <*|identifier> [identifier...] or include <*|identifier> -> <*|identifier>", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_ThrowsAnException_WhenTheSpecifiedElementDoesNotExist() {
        try {
            parser.parseInclude(new DeploymentViewDslContext(null), tokens("include", "user"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element/relationship \"user\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parseInclude_AddsAllDeploymentNodesAndChildrenInTheDeploymentEnvironment_WhenTheWildcardIsSpecifiedAndTheViewHasNoSoftwareSystemScope() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        Container c1 = ss1.addContainer("C1", "Description", "Technology");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Container c2 = ss2.addContainer("C2", "Description", "Technology");

        DeploymentNode dev1 = model.addDeploymentNode("Dev", "Dev 1", "Description", "Technology");
        DeploymentNode dev2 = dev1.addDeploymentNode("Dev 2", "Description", "Technology");
        InfrastructureNode dev3 = dev2.addInfrastructureNode("Dev 3", "Description", "Technology");
        ContainerInstance dev4 = dev2.add(c1);
        ContainerInstance dev5 = dev2.add(c2);

        DeploymentNode live1 = model.addDeploymentNode("Live", "Live 1", "Description", "Technology");
        DeploymentNode live2 = live1.addDeploymentNode("Live 2", "Description", "Technology");
        InfrastructureNode live3 = live2.addInfrastructureNode("Live 3", "Description", "Technology");
        ContainerInstance live4 = live2.add(c1);
        ContainerInstance live5 = live2.add(c2);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "*"));

        assertEquals(5, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live1)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live2)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live3)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live4)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live5)));
    }

    @Test
    void test_parseInclude_AddsAllDeploymentNodesAndChildrenInTheDeploymentEnvironment_WhenTheWildcardIsSpecifiedAndTheViewHasASoftwareSystemScope() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        Container c1 = ss1.addContainer("C1", "Description", "Technology");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Container c2 = ss2.addContainer("C2", "Description", "Technology");

        DeploymentNode dev1 = model.addDeploymentNode("Dev", "Dev 1", "Description", "Technology");
        DeploymentNode dev2 = dev1.addDeploymentNode("Dev 2", "Description", "Technology");
        InfrastructureNode dev3 = dev2.addInfrastructureNode("Dev 3", "Description", "Technology");
        ContainerInstance dev4 = dev2.add(c1);
        ContainerInstance dev5 = dev2.add(c2);

        DeploymentNode live1 = model.addDeploymentNode("Live", "Live 1", "Description", "Technology");
        DeploymentNode live2 = live1.addDeploymentNode("Live 2", "Description", "Technology");
        InfrastructureNode live3 = live2.addInfrastructureNode("Live 3", "Description", "Technology");
        ContainerInstance live4 = live2.add(c1);
        ContainerInstance live5 = live2.add(c2);

        DeploymentView view = views.createDeploymentView(ss1, "key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        parser.parseInclude(context, tokens("include", "*"));

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live1)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live2)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live3)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live4)));
    }

    @Test
    void test_parseInclude_AddsTheSpecifiedElements() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        Container c1 = ss1.addContainer("C1", "Description", "Technology");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Container c2 = ss2.addContainer("C2", "Description", "Technology");
        CustomElement box1 = model.addCustomElement("Box 1");

        DeploymentNode dev1 = model.addDeploymentNode("Dev", "Dev 1", "Description", "Technology");
        DeploymentNode dev2 = dev1.addDeploymentNode("Dev 2", "Description", "Technology");
        InfrastructureNode dev3 = dev2.addInfrastructureNode("Dev 3", "Description", "Technology");
        ContainerInstance dev4 = dev2.add(c1);
        ContainerInstance dev5 = dev2.add(c2);

        DeploymentNode live1 = model.addDeploymentNode("Live", "Live 1", "Description", "Technology");
        DeploymentNode live2 = live1.addDeploymentNode("Live 2", "Description", "Technology");
        InfrastructureNode live3 = live2.addInfrastructureNode("Live 3", "Description", "Technology");
        ContainerInstance live4 = live2.add(c1);
        ContainerInstance live5 = live2.add(c2);

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", live1);
        elements.put("box1", box1);

        DeploymentView view = views.createDeploymentView(ss1, "key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);
        context.setElements(elements);

        parser.parseInclude(context, tokens("include", "element", "box1"));

        assertEquals(5, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live1)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live2)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live3)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(live4)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(box1)));
    }

    @Test
    void test_parseInclude_AddsTheElement_WhenTheElementIsAnInfrastructureNode() {
        DeploymentNode dn = model.addDeploymentNode("Live", "DN", "Description", "Technology");
        InfrastructureNode in = dn.addInfrastructureNode("IN", "Description", "Technology");

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", in);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);
        context.setElements(elements);

        parser.parseInclude(context, tokens("include", "element"));

        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(dn));
        assertNotNull(view.getElementView(in));
    }

    @Test
    void test_parseInclude_AddsTheElement_WhenTheElementIsASoftwareSystemInstance() {
        DeploymentNode dn = model.addDeploymentNode("Live", "DN", "Description", "Technology");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        SoftwareSystemInstance softwareSystemInstance = dn.add(softwareSystem);

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", softwareSystemInstance);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);
        context.setElements(elements);

        parser.parseInclude(context, tokens("include", "element"));

        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(dn));
        assertNotNull(view.getElementView(softwareSystemInstance));
    }

    @Test
    void test_parseInclude_AddsTheElement_WhenTheElementIsAContainerInstance() {
        DeploymentNode dn = model.addDeploymentNode("Live", "DN", "Description", "Technology");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        ContainerInstance containerInstance = dn.add(container);

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", containerInstance);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);
        context.setElements(elements);

        parser.parseInclude(context, tokens("include", "element"));

        assertEquals(2, view.getElements().size());
        assertNotNull(view.getElementView(dn));
        assertNotNull(view.getElementView(containerInstance));
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheNoElementsAreSpecified() {
        try {
            parser.parseExclude(new DeploymentViewDslContext(null), tokens("exclude"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("Expected: exclude <identifier> [identifier...] or exclude <*|identifier> -> <*|identifier>", iae.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheSpecifiedElementDoesNotExist() {
        try {
            parser.parseExclude(new DeploymentViewDslContext(null), tokens("exclude", "user"));
            fail();
        } catch (RuntimeException iae) {
            assertEquals("The element/relationship \"user\" does not exist", iae.getMessage());
        }
    }

    @Test
    void test_parseExclude_RemovesTheSpecifiedElement() {
        DeploymentNode dn = model.addDeploymentNode("Live", "DN", "Description", "Technology");
        InfrastructureNode in = dn.addInfrastructureNode("IN", "Description", "Technology");

        Map<String, Element> elements = new HashMap<>();
        elements.put("element", in);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        view.addAllDeploymentNodes();
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);
        context.setElements(elements);

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(dn)));
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(in)));

        parser.parseExclude(context, tokens("exclude", "element"));

        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().stream().anyMatch(ev -> ev.getElement().equals(dn)));
    }

    @Test
    void test_parseExclude_ExcludesReplicatedVersionsOfTheSpecifiedRelationship() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Relationship rel = ss1.uses(ss2, "Uses");

        DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
        dn.add(ss1);
        dn.add(ss2);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);
        
        Map<String, Relationship> relationships = new HashMap<>();
        relationships.put("rel", rel);
        context.setRelationships(relationships);

        view.addDefaultElements();
        assertEquals(1, view.getRelationships().stream().map(RelationshipView::getRelationship).filter(r -> r.getLinkedRelationshipId().equals(rel.getId())).count());

        parser.parseExclude(context, tokens("exclude", "rel"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipSourceElementDoesNotExistInTheModel() {
        try {
            SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
            SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
            Relationship rel = ss1.uses(ss2, "Uses");

            DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
            dn.add(ss1);
            dn.add(ss2);

            DeploymentView view = views.createDeploymentView("key", "Description");
            view.setEnvironment("Live");
            DeploymentViewDslContext context = new DeploymentViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("ss2", ss2);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "ss1", "->", "ss2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"ss1\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipSourceElementDoesNotExistInTheView() {
        try {
            SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
            SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
            Relationship rel = ss1.uses(ss2, "Uses");

            DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");

            DeploymentView view = views.createDeploymentView("key", "Description");
            view.setEnvironment("Live");
            DeploymentViewDslContext context = new DeploymentViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("ss1", ss1);
            elements.put("ss2", ss2);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "ss1", "->", "ss2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"ss1\" does not exist in the view", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipDestinationElementDoesNotExistInTheModel() {
        try {
            SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
            SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
            Relationship rel = ss1.uses(ss2, "Uses");

            DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
            dn.add(ss1);

            DeploymentView view = views.createDeploymentView("key", "Description");
            view.setEnvironment("Live");
            view.add(dn);
            DeploymentViewDslContext context = new DeploymentViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("ss1", ss1);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "ss1", "->", "ss2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"ss2\" does not exist", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_ThrowsAnException_WhenTheRelationshipDestinationElementDoesNotExistInTheView() {
        try {
            SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
            SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
            Relationship rel = ss1.uses(ss2, "Uses");

            DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
            dn.add(ss1);

            DeploymentView view = views.createDeploymentView("key", "Description");
            view.setEnvironment("Live");
            view.add(dn);
            DeploymentViewDslContext context = new DeploymentViewDslContext(view);
            context.setWorkspace(workspace);

            Map<String, Element> elements = new HashMap<>();
            elements.put("ss1", ss1);
            elements.put("ss2", ss2);
            context.setElements(elements);

            parser.parseExclude(context, tokens("exclude", "ss1", "->", "ss2"));

            fail();
        } catch (RuntimeException re) {
            assertEquals("The element \"ss2\" does not exist in the view", re.getMessage());
        }
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSourceAndDestination() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Relationship rel = ss1.uses(ss2, "Uses");

        DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
        dn.add(ss1);
        dn.add(ss2);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        view.add(dn);
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("ss1", ss1);
        elements.put("ss2", ss2);
        context.setElements(elements);

        parser.parseExclude(context, tokens("exclude", "ss1", "->", "ss2"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithSourceAndWildcard() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Relationship rel = ss1.uses(ss2, "Uses");

        DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
        dn.add(ss1);
        dn.add(ss2);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        view.add(dn);
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("ss1", ss1);
        elements.put("ss2", ss2);
        context.setElements(elements);

        parser.parseExclude(context, tokens("exclude", "ss1", "->", "*"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithWildcardAndDestination() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Relationship rel = ss1.uses(ss2, "Uses");

        DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
        dn.add(ss1);
        dn.add(ss2);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        view.add(dn);
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("ss1", ss1);
        elements.put("ss2", ss2);
        context.setElements(elements);

        parser.parseExclude(context, tokens("exclude", "*", "->", "ss2"));
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_parseExclude_RemovesTheRelationshipFromAView_WhenAnExpressionIsSpecifiedWithWildcardAndWildcard() {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "Description");
        SoftwareSystem ss2 = model.addSoftwareSystem("SS2", "Description");
        Relationship rel = ss1.uses(ss2, "Uses");

        DeploymentNode dn = model.addDeploymentNode("Live", "Live", "Description", "Technology");
        dn.add(ss1);
        dn.add(ss2);

        DeploymentView view = views.createDeploymentView("key", "Description");
        view.setEnvironment("Live");
        view.add(dn);
        DeploymentViewDslContext context = new DeploymentViewDslContext(view);
        context.setWorkspace(workspace);

        Map<String, Element> elements = new HashMap<>();
        elements.put("ss1", ss1);
        elements.put("ss2", ss2);
        context.setElements(elements);

        parser.parseExclude(context, tokens("exclude", "*", "->", "*"));
        assertEquals(0, view.getRelationships().size());
    }

}