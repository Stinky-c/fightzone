package com.buckydev.config.snakeyaml;

import com.buckydev.config.FightzoneConfig;
import org.apache.commons.jexl3.JexlScript;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class Represented extends Representer {

    public static final Tag SCRIPT_TAG = new Tag("!script");


    public Represented(DumperOptions options) {
        super(options);
        this.multiRepresenters.put(JexlScript.class, this::representScript);
        this.addClassTag(FightzoneConfig.class, Tag.MAP);

    }

    private Node representScript(Object data) {
        JexlScript script = (JexlScript) data;
        return representScalar(SCRIPT_TAG, script.getSourceText());
    }


}