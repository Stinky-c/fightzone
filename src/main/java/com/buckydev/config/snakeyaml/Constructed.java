package com.buckydev.config.snakeyaml;

import com.buckydev.script.Executor;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class Constructed extends Constructor {


    public Constructed(Class<?> theRoot, LoaderOptions loadingConfig) {
        super(theRoot, loadingConfig);
        this.yamlConstructors.put(new Tag("!script"), new ConstructScript());
    }


    private class ConstructScript extends AbstractConstruct {

        @Override
        public Object construct(Node node) {
            if (!(node instanceof ScalarNode scalarNode)) {
                throw new YAMLException("!script must be a scalar");
            }
            String source = constructScalar(scalarNode);
            return Executor.compileScript(source);
        }
    }
}
