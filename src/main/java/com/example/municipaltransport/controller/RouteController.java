package com.example.municipaltransport.controller;

import com.example.municipaltransport.model.Route;
import com.example.municipaltransport.repository.RouteRepository;
import com.example.municipaltransport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RestController
@RequestMapping("/routes/*")
public class RouteController {

    @Autowired
    private RouteService routeService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RouteRepository routeRepository;
    private List<Route> routeList;

    @GetMapping
    public ResponseEntity<List<Route>> getRoutes() {
        List<Route> routeList = routeService.getAll();
        System.out.println(routeList);
        return ResponseEntity.ok(routeList);
    }

    @PostMapping
    public ResponseEntity<List<Route>> createRoute(@RequestBody Route route) {
        routeService.save(route);
        return ResponseEntity.status(201).body(routeList);
    }

    @GetMapping("/Start/{startLocation}")
    public List<Route> getRouteByStartLocation(@PathVariable String startLocation) {
        List<Route> routes = routeService.findByStartLocation(startLocation);
        return routes;
    }

    @GetMapping("/End/{endLocation}")
    public List<Route> getRouteByEndLocation(@PathVariable String endLocation) {
        List<Route> routes = routeService.findByEndLocation(endLocation);
        return routes;
    }
    @GetMapping("/favorites")
    public ResponseEntity<List<Route>> getFavoriteRoutes() {
        List<Route> favoriteRoutes = routeService.findByIsFavorite();
        return ResponseEntity.ok(favoriteRoutes);
    }

    @PutMapping("/favorite/{id}")

    public ResponseEntity<Route> markAsFavorite(@PathVariable Long id) {
        Optional<Route> route = routeService.findById(id);
        if (route.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Route existingRoute = route.get();
        existingRoute.setFavorite(true);

        Route updatedRoute = routeService.save(existingRoute);
        return ResponseEntity.ok(updatedRoute);
    }

    @PutMapping("/unmark-favorite/{id}")

    public ResponseEntity<Route> unmarkAsFavorite(@PathVariable Long id) {
        Optional<Route> route = routeService.findById(id);
        if (route.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Route existingRoute = route.get();
        existingRoute.setFavorite(false);

        Route updatedRoute = routeService.save(existingRoute);
        return ResponseEntity.ok(updatedRoute);
    }

    @PutMapping("/delay/{id}")
    public ResponseEntity<Route> routeDelay(@PathVariable Long id, @RequestBody Map<String, Integer> delay) {
        int newDelay = delay.get("delay");
        Route route = routeService.updateDelay(id, newDelay);
        return ResponseEntity.ok(route);
    }
    @PutMapping("/{id}/delayDescription")
    public ResponseEntity<Route> routeDelayDescription(@PathVariable Long id, @RequestBody Map<String, Integer> description) {
        String newDescription = String.valueOf(description.get("description"));
        Route route = routeService.addDescription(id, newDescription);
        return ResponseEntity.ok(route);
    }
    @GetMapping("/{startLocation}/to/{endLocation}")
    public ResponseEntity<List<Route>> getRouteByStartAndEndLocation(@PathVariable String startLocation, @PathVariable String endLocation) {
        List<Route> routes = routeService.findByStartAndEndLocation(startLocation, endLocation);
        return ResponseEntity.ok(routes);


    }
}

