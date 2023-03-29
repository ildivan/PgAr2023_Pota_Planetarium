package planetarium;

import planetarium.solarsystem.SolarSystem;

public class Planetarium {
    public static void main(String[] args) {
        var system = new SolarSystem(0,0,3);
        var star = system.getStar();

        for (int i = 0; i < 1000; i++) {
            star.addNewPlanet(i,i,i);
        }

        for(var planet : star.getPlanets()){
            System.out.println(planet.toString());
            for (int i = 0; i < 1000; i++) {
                planet.addNewMoon(i,i,i);
            }
            for(var moon : planet.getMoons()){
                System.out.println(moon.toString());
            }
        }
    }
}
