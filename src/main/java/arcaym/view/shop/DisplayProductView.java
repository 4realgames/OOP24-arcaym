package arcaym.view.shop;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import arcaym.model.game.objects.GameObjectType;
import arcaym.view.components.HorizontalCenteredPanel;
import arcaym.view.core.ViewComponent;
import arcaym.view.objects.GameObjectView;
import arcaym.view.scaling.WindowInfo;
import arcaym.view.utils.SwingUtils;

/**
 * View of products with their description.
 */
public class DisplayProductView implements ViewComponent<JPanel> {
    private static final Color TITLE_COLOR = Color.WHITE;
    private final GameObjectType productType;

    /**
     * Contructor of single ProductView.
     * 
     * @param info
     */
    public DisplayProductView(final ProductInfo info) {
        this.productType = info.type();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel build(final WindowInfo window) {
        final JPanel contenPanel = new JPanel();
        contenPanel.setLayout(new BoxLayout(contenPanel, BoxLayout.PAGE_AXIS));
        final JPanel productName = new HorizontalCenteredPanel().build(window, new JLabel(productType.name()));
        productName.setBackground(TITLE_COLOR);
        contenPanel.add(productName);
        final JLabel productImgae = new GameObjectView(productType, 4).build(window);
        final JPanel productImagePanel = new HorizontalCenteredPanel().build(window, productImgae);
        contenPanel.add(Box.createVerticalStrut(SwingUtils.getBigGap(contenPanel)));
        contenPanel.add(productImagePanel);
        contenPanel.add(Box.createVerticalStrut(SwingUtils.getBigGap(contenPanel)));
        return new HorizontalCenteredPanel().build(window, contenPanel);
    }

}
