package project.app.player;

import project.app.coms.Client;

public class HumanPlayer extends Player {
    private Client client;
    public HumanPlayer() {

    }
    public HumanPlayer(boolean isJudge, int id) {
        super(isJudge, id);
    }

    public HumanPlayer(boolean isJudge, int id, Client client) {
        super(isJudge, id);
        this.client = client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
