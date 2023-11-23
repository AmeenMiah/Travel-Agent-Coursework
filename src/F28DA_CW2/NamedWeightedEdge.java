package F28DA_CW2;

import org.jgrapht.graph.DefaultWeightedEdge;

public class NamedWeightedEdge extends DefaultWeightedEdge {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
