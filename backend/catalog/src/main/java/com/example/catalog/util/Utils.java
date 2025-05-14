package com.example.catalog.util;

import com.example.catalog.projections.IdProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered
            , List<? extends IdProjection<ID>> unordered) {
        Map<ID, IdProjection<ID>> map = new HashMap<>();
        for (IdProjection<ID> p : unordered) map.put(p.getId(), p);

        List<IdProjection<ID>> result = new ArrayList<>();
        for (IdProjection<ID> p : ordered) result.add(map.get(p.getId()));
        return result;
    }
}
