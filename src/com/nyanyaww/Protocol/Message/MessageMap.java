package com.nyanyaww.Protocol.Message;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author nyanyaww
 * @program modbus
 * @description 清晰的信息类，把信息看成字典
 * @create 2019-06-02 02:00
 **/
public class MessageMap {
    Map<String, Character> message = new Map<String, Character>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Character get(Object key) {
            return null;
        }

        @Override
        public Character put(String key, Character value) {
            return null;
        }

        @Override
        public Character remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends Character> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<String> keySet() {
            return null;
        }

        @Override
        public Collection<Character> values() {
            return null;
        }

        @Override
        public Set<Entry<String, Character>> entrySet() {
            return null;
        }
    };
}
