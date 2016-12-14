public class ClientInfo {
    private int id;
    private Client clientStub;

    public ClientInfo(int id, Client stub) {
	this.id = id;
	this.clientStub = stub;
    }

    public int getId() {
	return this.id;
    }

    public Client getStub() {
	return this.clientStub;
    }
}
