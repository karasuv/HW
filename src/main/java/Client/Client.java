package Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

//шаблон клиента для сетевого хранилища
public class Client {

    public static void main(String[] args) throws InterruptedException {
        new Client().start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap client = new Bootstrap();
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>(){

                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder(),new StringEncoder(),new ClientDecoder());

                        }
                    });
            ChannelFuture future = client.connect("localhost",9000).sync();

            System.out.println("client started");
            while (true) {
                future.channel().writeAndFlush("hello from client").sync();

                Thread.sleep(5000);
            }

        } finally {
            group.shutdownGracefully();
        }


    }
}
