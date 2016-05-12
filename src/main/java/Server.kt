import net.NetworkManager

/**
 * Created by Kattoor on 11/05/2016.
 */

class Server(private val port: Int) {

    private val networkManager: NetworkManager;

    init {
        networkManager = NetworkManager(43594)
    }
}

fun main(args: Array<String>) {
    Server(43594)
}