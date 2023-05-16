package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.models.UserData

@Dao
interface ConnectionsDao {
//    @Query("SELECT * FROM connections")
//    fun getAllConnections(): LiveData<List<Connections>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addConnection(connection: Connections)


//    @Query(
//        "DELETE FROM connections " +
//                "WHERE " +
//                "((user_id = :userId OR user_id = :connectedUserId) " +
//                "AND (connected_user_id = :userId or connected_user_id = :connectedUserId))"
//    )
//    fun removeConnection(userId: Int, connectedUserId: Int)

    @Query(
        "DELETE FROM connections " +
                "WHERE " +
                "((user_id = :userId AND connected_user_id = :connectedUserId) " +
                "OR (user_id = :connectedUserId AND connected_user_id = :userId))"
    )
    suspend fun removeConnection(userId: Int, connectedUserId: Int)


//    @Query(
//        "SELECT c1.connected_user_id FROM connections c1 " +
//                "INNER JOIN connections c2 " +
//                "ON c1.connected_user_id = c2.user_id " +
//                "AND c2.connected_user_id = :userId " +
//                "WHERE c1.user_id = :userId " +
//                "AND c1.status = 'requested' " +
//                "AND c2.status = 'requested'"
//    )

    @Query(
        "SELECT connected_user_id\n" +
                "FROM connections\n" +
                "WHERE user_id = :userId AND status = 'CONNECTED'\n" +
                "\n" +
                "UNION\n" +
                "\n" +
                "SELECT user_id\n" +
                "FROM connections\n" +
                "WHERE connected_user_id = :userId AND status = 'CONNECTED'"
    )
    fun getConnectedUserIds(userId: Int): LiveData<List<Int>>


    //    @Query("SELECT connected_user_id\n" +
//            "FROM connections\n" +
//            "WHERE user_id = :userId AND status = 'REQUESTED'\n" +
//            "\n" +
//            "UNION\n" +
//            "\n" +
//            "SELECT user_id\n" +
//            "FROM connections\n" +
//            "WHERE connected_user_id = :userId AND status = 'REQUESTED'")
    @Query(
        "SELECT user_id\n" +
                "FROM connections\n" +
                "WHERE connected_user_id = :connectedUserId AND status = 'REQUESTED'\n"
//            "\n" +
//            "UNION\n" +
//            "\n" +
//            "SELECT user_id\n" +
//            "FROM connections\n" +
//            "WHERE connected_user_id = :userId AND status = 'REQUESTED'"
    )
    fun getConnectionRequests(connectedUserId: Int): LiveData<List<Int>>




    @Query(
        "SELECT EXISTS (SELECT * FROM connections " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR  (user_id= :connectedUserId AND connected_user_id= :userId))"
    )
    suspend fun isConnectionAvailable(userId: Int, connectedUserId: Int): Boolean



    @Query("SELECT EXISTS (SELECT * FROM connections " +
            "WHERE (connected_user_id= :userId AND status='REQUESTED'))")
    suspend fun isRequestsPending(userId: Int):Boolean

    @Query(
        "SELECT status " +
                "FROM connections " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR (user_id= :connectedUserId AND connected_user_id= :userId)"
    )
    fun getConnectionStatus(userId: Int, connectedUserId: Int): LiveData<String?>

    @Query(
        "SELECT * " +
                "FROM connections " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR (user_id= :connectedUserId AND connected_user_id= :userId)"
    )
    fun getConnectionDetails(userId: Int, connectedUserId: Int): LiveData<Connections?>


    @Query(
        "UPDATE connections " +
                "SET status = :status " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR " +
                "(user_id= :connectedUserId AND connected_user_id= :userId)"
    )
    suspend fun setConnectionStatus(userId: Int, connectedUserId: Int, status: String)


    @Query(
        "SELECT user_id as userId,name,dob,religion,caste,state,city,height,profile_pic,education,occupation " +
                "FROM user " +
                "WHERE user_id IN " +
                "(" +
                "SELECT connected_user_id " +
                "FROM connections " +
                "WHERE user_id = :userId AND status = 'CONNECTED' " +
                "UNION " +
                "SELECT user_id " +
                "FROM connections " +
                "WHERE connected_user_id = :userId AND status = 'CONNECTED'" +
                ")"
    )
    fun getConnectedUsers(userId: Int): LiveData<List<UserData>>

//    @Query("SELECT")
//    fun getConnectedUserIds(userId: Int):LiveData<List<Int>>

//    @PrimaryKey
//    val id: Int=0,
//    val user_id: String,
//    val connected_user_id: String,
//    val status: String

}