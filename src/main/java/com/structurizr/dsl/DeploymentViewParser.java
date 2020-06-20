package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DeploymentView;

final class DeploymentViewParser extends AbstractParser {

    private static final String VIEW_TYPE = "Deployment";

    private static final int SCOPE_IDENTIFIER_INDEX = 1;
    private static final int ENVIRONMENT_INDEX = 2;
    private static final int KEY_INDEX = 3;
    private static final int DESCRIPTION_INDEX = 4;

    DeploymentView parse(DslContext context, Tokens tokens) {
        // deployment <*|software system identifier> <environment name> [key] [description] {

        if (!tokens.includes(ENVIRONMENT_INDEX)) {
            throw new RuntimeException("Expected: deployment <*|software system identifier> <environment name> [key] [description] {");
        }

        Workspace workspace = context.getWorkspace();
        String key = "";

        if (tokens.includes(KEY_INDEX)) {
            key = tokens.get(KEY_INDEX);
        } else {
            key = generateViewKey(workspace, VIEW_TYPE);
        }
        validateViewKey(key);

        String scopeIdentifier = tokens.get(SCOPE_IDENTIFIER_INDEX);
        String environment = tokens.get(ENVIRONMENT_INDEX);

        String description = "";

        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        DeploymentView view;

        if ("*".equals(scopeIdentifier)) {
            view = workspace.getViews().createDeploymentView(key, description);
        } else {
            Element element = context.getElement(scopeIdentifier);
            if (element == null) {
                throw new RuntimeException("The software system \"" + scopeIdentifier + "\" does not exist");
            }

            if (element instanceof SoftwareSystem) {
                view = workspace.getViews().createDeploymentView((SoftwareSystem)element, key, description);
            } else {
                throw new RuntimeException("The element \"" + scopeIdentifier + "\" is not a software system");
            }
        }

        view.setEnvironment(environment);

        return view;
    }

}