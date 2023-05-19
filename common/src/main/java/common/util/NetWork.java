package common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;

public class NetWork {
	private static final Logger log = LogManager.getLogger(NetWork.class);
	@Cacheable(value = "NetWork.getLocalIp")
	public static final String getLocalIp() {
		try {
			List<InetAddress> lstIps = new ArrayList<InetAddress>();
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					// if (!inetAddress.isLoopbackAddress() && !inetAddress.isAnyLocalAddress()&&
					// !inetAddress.isSiteLocalAddress())
					if (inetAddress.isSiteLocalAddress())
						lstIps.add(inetAddress);
				}
			}
			//lstIps.add(InetAddress.getLocalHost());
			if (lstIps.isEmpty())
				return null;
			String result = lstIps.stream().map(InetAddress::getHostAddress).collect(Collectors.joining(", "));
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
}
