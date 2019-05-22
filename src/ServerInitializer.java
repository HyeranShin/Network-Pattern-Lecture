import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

public class ServerInitializer {
    public static void main(String[] args) {
        int port = 5000;
        System.out.println("Server ON : " + port);
        Reactor reactor = new Reactor(port);

        try{
            Serializer serializer = new Persister();
            File source = new File("HandlerList.xml");

            //simpleframework에서 제공하는 시리얼라이즈를 통해서 읽어온 파일을 통해서 자동으로 매칭해 주어서 시리얼라이즈가 가능
            ServerListData serverList = serializer.read(ServerListData.class, source);
            for (HandlerListData handlerListData : serverList.getServer()){
                if("server1".equals(handlerListData.getName())) {
                    List<HandlerData> handlerList = handlerListData.getHandler();
                    for(HandlerData handler : handlerList){
                        try{
                            reactor.registerHandler(handler.getHeader(), (EventHandler) Class.forName(handler.getHandler()).newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        reactor.startServer();
    }
}
