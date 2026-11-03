package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {

        private static final long serialVersionUID = 1L;

        Map<Integer, String> files = new HashMap<>();
        int nextId = 1;
    }

