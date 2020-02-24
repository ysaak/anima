package ysaak.anima.view.router;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

final class Route {

    private final String name;

    private final String path;

    private final List<String> paramList;

    Route(String name, String path, List<String> paramList) {
        this.name = name;
        this.path = path;
        this.paramList = paramList;
    }

    String getName() {
        return name;
    }

    String getPath() {
        return path;
    }

    List<String> getParamList() {
        return paramList;
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
                .add("params=" + paramList)
                .toString();
    }
}
