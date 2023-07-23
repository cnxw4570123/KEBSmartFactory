package inha.smartfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

@SpringBootApplication
public class SmartFactoryApplication {

    public static void main(String[] args) throws SocketException {
        SpringApplication.run(SmartFactoryApplication.class, args);
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println("i.getHostAddress() = " + i.getHostAddress());
            }
        }
    }

}
