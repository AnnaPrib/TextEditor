import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import ui.TextEditor;

public class TextEditorPlugin implements ApplicationComponent{

    public TextEditorPlugin() {}

    public String getComponentName() {
        return "Text Editor";
    }

    public void initComponent() {
        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerListener() {
            Key key = new Key("texteditorid");

            public void projectOpened(final Project project) {
                final TextEditor textEditor = new TextEditor();
                final ToolWindowManager twm = ToolWindowManager.getInstance(project);

                Runnable task1 = new Runnable() {
                    @Override
                    public void run() {
                        ToolWindow toolWindow = twm.registerToolWindow("Text Editor", false, ToolWindowAnchor.RIGHT);
                        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
                        Content content = contentFactory.createContent(textEditor.getRootComponent(), "", false);
                        toolWindow.getContentManager().addContent(content);
                        project.putUserData(key, textEditor);
                    }
                };
                twm.invokeLater(task1);
            }

            public void projectClosed(final Project project) {
            }
        });
    }

    public void disposeComponent() {}
}