package edu.cwru.eecs.clipper.Application;

import static spark.Spark.get;

import com.google.common.base.MoreObjects;
import edu.cwru.eecs.clipper.DataManagers.InitialDataManager;
import edu.cwru.eecs.clipper.DataManagers.UserAccountDataManager;
import edu.cwru.eecs.clipper.Models.UserAccount;
import spark.Request;
import spark.Response;

public class App {

  public static void main(String[] args) {
    get("/", (req, res) -> generateFeedback());
    get("/experiment_reset", (req, res) -> experimentalReset());
    get("/create_user_test", (req, res) -> createUser(res));
    get("/get_user_test", (req, res) -> getUser(req));
  }


  private static String generateFeedback() {
    System.out.println("Generating feedback");
    String feedback = "Hello!";
    InitialDataManager initialDataManager = new InitialDataManager();
    int sequence = (int) (Math.random() * 1000);
    String id = initialDataManager.write("" + sequence);
    feedback = feedback + ("\nInserted SEQ# = " + sequence);
    feedback = feedback + ("\nInserted @" + id);
    String seqout = initialDataManager.read(id);
    feedback = feedback + ("\nSaved" + seqout);
    return feedback;
  }

  private static String experimentalReset() {
    InitialDataManager.CreateCollection();
    return "resetted";
  }

  private static String createUser(Response res){
    UserAccountDataManager userAccountDataManager = new UserAccountDataManager();
    UserAccount account = userAccountDataManager.createUserAccount();
    System.out.println(MoreObjects.toStringHelper(account));
    account.setEmail("zxh108@test.case.edu");
    System.out.println(MoreObjects.toStringHelper(account));
    userAccountDataManager.updateUserAccount(account);
    res.redirect("/get_user_test?u="+account.getUserId());
    return account.toString();
  }

  private static String getUser(Request request){
    String userId = request.queryMap("u").value();
    return new UserAccountDataManager().findByUserID(userId).toString();
  }
}
