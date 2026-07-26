package com.buckydev.script;

import com.buckydev.script.Loader.ScriptMap;
import java.util.stream.Stream;
import org.apache.commons.jexl3.JexlExpression;

/// Holds the expression map and caches executor results
public class Store {

    private ScriptMap scriptMap;

    protected Store() {
        this.scriptMap = new ScriptMap();
    }

    public Stream<JexlExpression> iter() {
        return scriptMap.values().stream();
    }

    public void init(ScriptMap map) {
        scriptMap.clear();
        scriptMap.putAll(map);
    }
    // TODO Cache
}
