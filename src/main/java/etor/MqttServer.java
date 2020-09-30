package etor;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.cpu.CpuCoreSensor;

public class MqttServer {

    public static void run(Vertx vertx){

        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(CpuCoreSensor.availableProcessors());

        vertx.deployVerticle(MqttServerVerticle.class, options);
    }
}
