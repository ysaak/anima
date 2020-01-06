package ysaak.anima.view.router;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class Route {

    public final String name;

    public final String path;

    public final List<String> params;

    Route(String name, String path, List<String> params) {
        this.name = name;
        this.path = path;
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return name.equals(route.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Route.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("path='" + path + "'")
                .add("params=" + params)
                .toString();
    }
}
