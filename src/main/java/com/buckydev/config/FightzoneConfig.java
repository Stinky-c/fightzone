package com.buckydev.config;


import com.buckydev.script.Executor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.util.EventResult;
import org.apache.commons.jexl3.JexlScript;

public class FightzoneConfig {

    private List<Script> scripts = defaultScript();
    private boolean saveEnabled = false;
    private boolean enabled = true;


    FightzoneConfig() {

    }

    private List defaultScript() {
        List list = new ArrayList();

        Script script = new Script();
        script.setName("HelloWorld!");
        script.setAction(EventResult.DENY);
        script.setScript("target == 'minecraft:player'");
        list.add(script);
        return list;
    }

    //region Getter/setter

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public void setSaveEnabled(boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }

    //endregion

    public static class Script implements Serializable {

        public Script() {
        }


        private String name;
        private JexlScript script;
        private EventResult action;


        //region Getter/setter
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public JexlScript getScript() {
            return script;
        }

        public void setScript(JexlScript script) {
            this.script = script;
        }

        public void setScript(String script) {
            this.script = Executor.compileScript(script);
        }

        public EventResult getAction() {
            return action;
        }

        public void setAction(EventResult action) {
            this.action = action;
        }

        //endregion

    }

}
