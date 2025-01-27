package arcaym.view.app.menu;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import arcaym.view.app.menu.levels.LevelsPanel;
import arcaym.view.app.panels.impl.PanelsSwitcher;
import arcaym.view.app.panels.impl.SwitchablePanel;
import arcaym.view.components.CenteredPanel;
import arcaym.view.components.ImageLabel;
import arcaym.view.utils.SwingUtils;

/**
 * View for the main menu.
 */
public class MainMenu extends SwitchablePanel {

    private static final String TITLE_IMAGE = "title.png";
    private static final String LEVELS_BUTTON_TEXT = "LOAD LEVELS";
    private static final String SHOP_BUTTON_TEXT = "OPEN SHOP";

    public MainMenu(final Consumer<Supplier<SwitchablePanel>> switcher) {
        super(switcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel build() {
        final var mainPanel = new JPanel();
        final var levelsButton = new MenuButton(LEVELS_BUTTON_TEXT).build();
        final var shopButton = new MenuButton(SHOP_BUTTON_TEXT).build();
        levelsButton.addActionListener(e -> this.switcher().accept(() -> new LevelsPanel(this.switcher())));
        final var gap = SwingUtils.getNormalGap(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(new CenteredPanel().build(new ImageLabel(TITLE_IMAGE)));
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(new CenteredPanel().build(levelsButton));
        mainPanel.add(Box.createVerticalStrut(gap));
        mainPanel.add(new CenteredPanel().build(shopButton));
        mainPanel.add(Box.createVerticalGlue());
        return new CenteredPanel().build(mainPanel);
    }

    /**
     * TODO remove.
     * @param args foiaeuighiouweahiuogfea
     */
    public static void main(final String[] args) {
        SwingUtils.testComponent(new PanelsSwitcher(
            switcher -> () -> new MainMenu(switcher)
        ).build());
    }

}
