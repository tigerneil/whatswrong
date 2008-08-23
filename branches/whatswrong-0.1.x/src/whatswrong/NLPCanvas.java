package whatswrong;


import net.sf.epsgraphics.ColorMode;
import net.sf.epsgraphics.EpsGraphics;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Sebastian Riedel
 */
public class NLPCanvas extends JPanel {

  private SpanLayout spanLayout = new SpanLayout();
  private DependencyLayout dependencyLayout = new DependencyLayout();
  private TokenLayout tokenLayout = new TokenLayout();

  private ArrayList<Token> tokens = new ArrayList<Token>();
  private LinkedList<Edge> dependencies = new LinkedList<Edge>();

  private BufferedImage tokenImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
  private BufferedImage dependencyImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
  private BufferedImage spanImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

  private EdgeTypeFilter dependencyTypeFilter = new EdgeTypeFilter();
  private EdgeLabelFilter dependencyLabelFilter = new EdgeLabelFilter();
  private EdgeTokenFilter dependencyTokenFilter = new EdgeTokenFilter();
  private TokenFilter tokenFilter = new TokenFilter();
  private Set<String> usedTypes = new HashSet<String>();
  private Set<TokenProperty> usedProperties = new java.util.HashSet<TokenProperty>();

  private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
  private boolean antiAliasing = true;
  private NLPInstance nlpInstance;

  public interface Listener {
    void instanceChanged();

    void redrawn();
  }

  private ArrayList<Listener> listeners = new ArrayList<Listener>();

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  public NLPCanvas() {
    setPreferredSize(new Dimension(300, 300));
    setOpaque(false);
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        point.translate(0, -(getHeight() - tokenLayout.getHeight() -
          dependencyLayout.getHeight() - spanLayout.getHeight()));
        Edge edge = dependencyLayout.getEdgeAt(point, 5);
        //System.out.println("edge = " + edge);
        if (edge != null) {
          if (e.isMetaDown())
            dependencyLayout.toggleSelection(edge);
          else
            dependencyLayout.select(edge);

          updateNLPGraphics();
        }
      }
    });
  }


  public boolean isAntiAliasing() {
    return antiAliasing;
  }

  public void setAntiAliasing(boolean antiAliasing) {
    this.antiAliasing = antiAliasing;
  }

  public void addChangeListenger(ChangeListener changeListener) {
    changeListeners.add(changeListener);
  }

  private void fireChanged() {
    ChangeEvent event = new ChangeEvent(this);
    for (ChangeListener changeListener : changeListeners) {
      changeListener.stateChanged(event);
    }
  }

  private void fireInstanceChanged() {
    for (Listener l : listeners) l.instanceChanged();
  }

  private void fireRedrawn() {
    for (Listener l : listeners) l.redrawn();
  }

  public EdgeTypeFilter getDependencyTypeFilter() {
    return dependencyTypeFilter;
  }


  public EdgeLabelFilter getDependencyLabelFilter() {
    return dependencyLabelFilter;
  }


  public EdgeTokenFilter getDependencyTokenFilter() {
    return dependencyTokenFilter;
  }


  public TokenFilter getTokenFilter() {
    return tokenFilter;
  }


  public void setNLPInstance(NLPInstance nlpInstance) {
    dependencies.clear();
    dependencies.addAll(nlpInstance.getEdges());
    usedTypes.clear();
    for (Edge edge : dependencies)
      usedTypes.add(edge.getType());
    tokens.clear();
    tokens.addAll(nlpInstance.getTokens());
    usedProperties.clear();
    for (Token token : tokens) {
      usedProperties.addAll(token.getPropertyTypes());
    }
    spanLayout.clearSelection();
    fireInstanceChanged();
    //updateNLPGraphics();
  }

  public Set<TokenProperty> getUsedProperties() {
    return Collections.unmodifiableSet(usedProperties);
  }

  public Set<String> getUsedTypes() {
    return Collections.unmodifiableSet(usedTypes);
  }

  public void setColorForDependencyType(String type, Color color) {
    spanLayout.setColor(type, color);
  }

  private NLPInstance filterInstance() {
    return dependencyTokenFilter.filter(dependencyLabelFilter.filter(dependencyTypeFilter.filter(
      tokenFilter.filter(new NLPInstance(tokens, dependencies)))));
  }

  public void updateNLPGraphics() {
    NLPInstance filtered = filterInstance();

    //get edges and tokens
    Collection<Token> tokens = new ArrayList<Token>(filtered.getTokens());
    Collection<Edge> dependencies = new ArrayList<Edge>(filtered.getEdges(Edge.RenderType.dependency));
    Collection<Edge> spans = new ArrayList<Edge>(filtered.getEdges(Edge.RenderType.span));

    //create dummy graphics objects to estimate dimensions    
    Graphics2D gTokens = tokenImage.createGraphics();
    Graphics2D gDependencies = dependencyImage.createGraphics();
    Graphics2D gSpans = spanImage.createGraphics();

    //get span required token widths
    Map<Token, Integer> widths = spanLayout.estimateRequiredTokenWidths(spans, gSpans);

    //test layout to estimate sizes
    tokenLayout.layout(tokens, widths, gTokens);
    dependencyLayout.layout(dependencies, tokenLayout, gDependencies);
    spanLayout.layout(spans, tokenLayout, gSpans);

    //create a new images with right dimensions
    tokenImage = new BufferedImage(tokenLayout.getWidth(), tokenLayout.getHeight(),
      BufferedImage.TYPE_4BYTE_ABGR);
    dependencyImage = new BufferedImage(dependencyLayout.getWidth(), dependencyLayout.getHeight(),
      BufferedImage.TYPE_4BYTE_ABGR);
    spanImage = new BufferedImage(spanLayout.getWidth(), spanLayout.getHeight(),
      BufferedImage.TYPE_4BYTE_ABGR);

    //create the real graphics objects
    gTokens = tokenImage.createGraphics();
    gDependencies = dependencyImage.createGraphics();
    gSpans = spanImage.createGraphics();

    //specify rendering hints
    if (antiAliasing) {
      gDependencies.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      gSpans.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    //do the layout
    tokenLayout.layout(tokens, widths, gTokens);
    dependencyLayout.layout(dependencies, tokenLayout, gDependencies);
    spanLayout.layout(spans, tokenLayout, gSpans);


    int width = spanLayout.getWidth();
    int height = dependencyLayout.getHeight() + tokenLayout.getHeight() + spanLayout.getHeight();
    setPreferredSize(new Dimension(width, height));
    setMinimumSize(new Dimension(width, height));
    setSize(new Dimension(width, getHeight()));
    repaint();
    invalidate();
    //invalidate();
    fireChanged();
    fireRedrawn();
  }

  private Collection<Token> filterTokens() {
    return tokenFilter.filterTokens(this.tokens);
  }

  private Collection<Edge> filterDependencies() {
    return dependencyTokenFilter.filterEdges(
      dependencyLabelFilter.filterEdges(
        dependencyTypeFilter.filterEdges(this.dependencies)));
  }


  public TokenLayout getTokenLayout() {
    return tokenLayout;
  }

  public SpanLayout getSpanLayout() {
    return spanLayout;
  }

  public DependencyLayout getDependencyLayout() {
    return dependencyLayout;
  }

  public void clear() {
    tokens.clear();
    dependencies.clear();
    usedTypes.clear();
  }


  public void paintComponent(Graphics graphics) {
    Graphics2D g2d = (Graphics2D) graphics;
    //g2d.setColor(Color.WHITE);
    //g2d.fillRect(0,0,getWidth(),getHeight());
    int y = getHeight() - dependencyImage.getHeight() - tokenImage.getHeight() - spanImage.getHeight();
    g2d.drawImage(dependencyImage, 0, y, this);
    g2d.drawImage(tokenImage, 0, y + dependencyImage.getHeight(), this);
    g2d.drawImage(spanImage, 0, y + tokenImage.getHeight() + dependencyImage.getHeight(), this);
  }


  public void exportToEPS(File file) throws IOException {

    EpsGraphics dummy = new EpsGraphics("Title", new ByteArrayOutputStream(), 0, 0,
      tokenLayout.getWidth(), spanLayout.getHeight() + tokenLayout.getHeight(), ColorMode.BLACK_AND_WHITE);

    NLPInstance filtered = filterInstance();

    //get edges and tokens
    Collection<Token> tokens = new ArrayList<Token>(filtered.getTokens());
    Collection<Edge> dependencies = new ArrayList<Edge>(filtered.getEdges(Edge.RenderType.dependency));
    Collection<Edge> spans = new ArrayList<Edge>(filtered.getEdges(Edge.RenderType.span));

    //create dummy graphics objects to estimate dimensions

    //get span required token widths
    Map<Token, Integer> widths = spanLayout.estimateRequiredTokenWidths(spans, dummy);

    //test layout to estimate sizes
    tokenLayout.layout(tokens, widths, dummy);
    dependencyLayout.layout(dependencies, tokenLayout, dummy);
    spanLayout.layout(spans, tokenLayout, dummy);

    //create actual EPS graphics object
    EpsGraphics g = new EpsGraphics("Title", new FileOutputStream(file), 0, 0,
      tokenLayout.getWidth() + 2,
      spanLayout.getHeight() + tokenLayout.getHeight() + dependencyLayout.getHeight(), 
      ColorMode.COLOR_RGB);

    // do eps rendering
    //g.translate(0, -spanLayout.getHeight()-tokenLayout.getHeight() - dependencyLayout.getHeight());
    dependencyLayout.layout(dependencies, tokenLayout, g);
    g.translate(0, dependencyLayout.getHeight());
    tokenLayout.layout(tokens, widths, g);
    g.translate(0, tokenLayout.getHeight());
    spanLayout.layout(spans, tokenLayout, g);

    g.flush();
    g.close();
    //To change body of created methods use File | Settings | File Templates.
  }
}
