package com.example.catalog.util;

import com.example.catalog.entities.Product;
import com.example.catalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unordered) {
        Map<Long, Product> map = new HashMap<>();
        for (Product p : unordered) map.put(p.getId(), p);

        List<Product> result = new ArrayList<>();
        for (ProductProjection p : ordered) result.add(map.get(p.getId()));
        return result;
    }
}
