package tabs.project.babypaste.bean;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Text;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author tabs
 */
@ManagedBean(name = "PasteBean")
@SessionScoped
public class PasteBean implements Serializable {
    
    private String poster;
    private String syntax;
    private Text textCode;
    private String paste_id;
    private final Map<String, String> brushMap;
    
    private String tempText;
    
    public PasteBean() {
        brushMap = new HashMap();
        brushMap.put("shBrushAS3.js", "as3");
        brushMap.put("shBrushBash.js", "bash");
        brushMap.put("shBrushColdFusion.js", "cf");
        brushMap.put("shBrushCSharp.js", "csharp");
        brushMap.put("shBrushCpp.js", "cpp");
        brushMap.put("shBrushCss.js", "css");
        brushMap.put("shBrushDelphi.js", "delphi");
        brushMap.put("shBrushDiff.js", "diff");
        brushMap.put("shBrushErlang.js", "erl");
        brushMap.put("shBrushGroovy.js", "groovy");
        brushMap.put("shBrushJScript.js", "js");
        brushMap.put("shBrushJava.js", "java");
        brushMap.put("shBrushJavaFX.js", "jfx");
        brushMap.put("shBrushPerl.js", "perl");
        brushMap.put("shBrushPhp.js", "php");
        brushMap.put("shBrushPlain.js", "plain");
        brushMap.put("shBrushPowerShell.js", "ps");
        brushMap.put("shBrushPython.js", "py");
        brushMap.put("shBrushRuby.js", "ruby");
        brushMap.put("shBrushScala.js", "scala");
        brushMap.put("shBrushSql.js", "sql");
        brushMap.put("shBrushVb.js", "vb");
        brushMap.put("shBrushXml.js", "xml");
        
    }
    
    public String submit() {
        Key pasteKey = KeyFactory.createKey("BabyPaste", "BabyPaste");
        
        String pid = String.format("%07d", new Random().nextInt(1000000) * 8);
        
        Entity paste = new Entity("Paste", pasteKey);
        paste.setProperty("paste_id", pid);
        paste.setProperty("poster", poster);
        paste.setProperty("syntax", syntax);
        paste.setProperty("codes", textCode);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(paste);
        
  
        return "view_paste.xhtml?faces-redirect=true&paste_id=" + pid;
    }


    // Getter and Setter methods
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getCode() {
        if (textCode == null)
            return "";
        
        return textCode.getValue();
    }

    public void setCode(String code) {
        this.textCode = new Text(code);
    }

    public String getBrush() {
        return brushMap.get(syntax);
    }

    public String getPaste_id() {
        return paste_id;
    }

    public void setPaste_id(String paste_id) {
        this.paste_id = paste_id;
        
        Key pasteKey = KeyFactory.createKey("BabyPaste", "BabyPaste");
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Filter idFilter =  new Query.FilterPredicate("paste_id", Query.FilterOperator.EQUAL, paste_id);
        Query query = new Query("Paste", pasteKey).setFilter(idFilter);
        List<Entity> lst = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
        
        for (Entity e: lst) {
            setCode(((Text) e.getProperty("codes")).getValue());
            setPoster(e.getProperty("poster").toString());
            setSyntax(e.getProperty("syntax").toString());
        }

    }
            
    
}
