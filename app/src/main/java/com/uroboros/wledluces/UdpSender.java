package com.uroboros.wledluces;

import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {
    private static final String TAG = "UdpSender";
    private static final int PORT = 21324;
    private String ip = "192.168.40.16";
    private int numLeds = 150;
    private DatagramSocket socket;
    private InetAddress address;
    private volatile boolean conectado = true;

    public UdpSender() {
        reconectar(ip, numLeds);
    }

    public synchronized void reconectar(String newIp, int leds) {
        this.ip = newIp;
        this.numLeds = leds;
        this.conectado = true;
        try {
            if (socket == null || socket.isClosed()) {
                socket = new DatagramSocket();
            }
            address = InetAddress.getByName(ip);
            Log.i(TAG, "Conectado WLED UDP en " + ip + ":" + PORT + " (" + leds + " LEDs)");
        } catch (Exception e) {
            Log.e(TAG, "Error resolviendo IP: " + e.getMessage());
        }
    }

    public synchronized void desconectar() {
        if (conectado) {
            try {
                // Enviar cuadro negro para apagar antes de desconectar
                byte[] off = new byte[numLeds * 3];
                enviarRaw(off);
            } catch (Exception ignored) {}
        }
        this.conectado = false;
        Log.i(TAG, "Desconectado de WLED");
    }

    public boolean isConectado() { return conectado; }

    public synchronized void enviar(byte[] rgb) {
        if (!conectado || socket == null || address == null || rgb == null) return;
        enviarRaw(rgb);
    }

    private void enviarRaw(byte[] rgb) {
        try {
            int len = rgb.length;
            if (len <= 490) {
                byte[] packet = new byte[len + 2];
                packet[0] = 2; // WLED DRGB protocol
                packet[1] = 2; // timeout in seconds
                System.arraycopy(rgb, 0, packet, 2, len);
                DatagramPacket dp = new DatagramPacket(packet, packet.length, address, PORT);
                socket.send(dp);
            } else {
                for (int s = 0; s < len; s += 489) {
                    int chunk = Math.min(489, len - s);
                    byte[] packet = new byte[chunk + 4];
                    packet[0] = 4; // DNRGB
                    packet[1] = 2;
                    packet[2] = (byte) ((s >> 8) & 0xFF);
                    packet[3] = (byte) (s & 0xFF);
                    System.arraycopy(rgb, s, packet, 4, chunk);
                    DatagramPacket dp = new DatagramPacket(packet, packet.length, address, PORT);
                    socket.send(dp);
                }
            }
        } catch (Exception ignored) {}
    }

    public String getIp() { return ip; }
    public int getNumLeds() { return numLeds; }
}
