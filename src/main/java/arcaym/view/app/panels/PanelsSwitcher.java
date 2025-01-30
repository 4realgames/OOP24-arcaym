package arcaym.view.app.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JPanel;

import arcaym.view.core.api.ScreenInfo;
import arcaym.view.core.api.ViewComponent;
import arcaym.view.utils.SwingUtils;

/**
 * View of a panel with content-switching capabilities.
 */
public class PanelsSwitcher implements ViewComponent<JPanel> {

    private static final String BACK_BUTTON_TEXT = "BACK";

    private final Deque<Supplier<SwitchablePanel>> panelsHistory = new LinkedList<>();
    private Optional<SwitchablePanel> currentPanel = Optional.empty();
    private final Function<Switcher, Supplier<SwitchablePanel>> rootProvider;

    /**
     * Initialize with given root panel.
     * 
     * @param rootProvider root provider
     */
    public PanelsSwitcher(
        final Function<Switcher, Supplier<SwitchablePanel>> rootProvider
    ) {
        this.rootProvider = Objects.requireNonNull(rootProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel build(final ScreenInfo screenInfo) {
        final var mainPanel = new JPanel(new BorderLayout());
        final var gap = SwingUtils.getNormalGap(mainPanel);
        final var topRow = new JPanel(new FlowLayout(FlowLayout.LEADING, gap, gap));
        final var backButton = new JButton(BACK_BUTTON_TEXT);
        topRow.add(backButton);
        backButton.addActionListener(e -> this.goToPrevious(screenInfo, mainPanel, topRow));
        this.addPanel(
            screenInfo,
            this.rootProvider.apply(s -> this.addPanel(screenInfo, s, mainPanel, topRow)),
            mainPanel, 
            topRow
        );
        return mainPanel;
    }

    private boolean canGoBack() {
        return this.panelsHistory.size() > 1;
    }

    private void goToPrevious(final ScreenInfo screenInfo, final JPanel mainPanel, final JPanel topRow) {
        if (this.canGoBack()) {
            this.panelsHistory.removeLast();
            this.setCurrentPanel(screenInfo, this.panelsHistory.peekLast(), mainPanel, topRow);
        }
    }

    private void addPanel(
        final ScreenInfo screenInfo,
        final Supplier<SwitchablePanel> panelSupplier,
        final JPanel mainPanel, 
        final JPanel topRow
    ) {
        this.panelsHistory.addLast(panelSupplier);
        this.setCurrentPanel(screenInfo, panelSupplier, mainPanel, topRow);
    }

    private void setCurrentPanel(
        final ScreenInfo screenInfo,
        final Supplier<SwitchablePanel> panelSupplier,
        final JPanel mainPanel, 
        final JPanel topRow
    ) {
        this.currentPanel.ifPresent(SwitchablePanel::close);
        this.currentPanel = Optional.of(panelSupplier.get());
        mainPanel.removeAll();
        if (this.canGoBack()) {
            mainPanel.add(topRow, BorderLayout.NORTH);
        }
        this.currentPanel.ifPresent(p -> mainPanel.add(p.build(screenInfo), BorderLayout.CENTER));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

}
