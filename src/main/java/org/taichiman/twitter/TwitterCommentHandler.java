package org.taichiman.twitter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.ListDirection;

public class TwitterCommentHandler {
    private static Jedis jedis;

    static {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public static void main(String[] args) {
        TwitterBot twitterBot = new TwitterBot("@KelleyKelli1", "Cf5e3SaKw");
        List<String> followers = new ArrayList<>(jedis.lrange("cryptoslaowai", 0, -1));
        //cryptoslaowai
        //jpegd_69
        //0xKillTheWolf
        //laoyu123123
        //MMCrypto
        twitterBot.comment(new HashSet<>(followers), 15, "https://twitter.com/LayerZero_Pro/status/1633744237450891265?s=20");
    }
}
