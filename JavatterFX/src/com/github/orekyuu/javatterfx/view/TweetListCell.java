package com.github.orekyuu.javatterfx.view;

import java.io.IOException;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.GroupBuilder;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import twitter4j.Status;

import com.github.orekyuu.javatterfx.controller.TweetObjectController;

public class TweetListCell extends ListCell<Status>{

	private Map<Long,Parent> map;

	public TweetListCell(Map<Long,Parent> map){
		this.map=map;
	}

	@Override
	public void updateItem(Status status,boolean flag){
		super.updateItem(status,flag);

		if(flag){
			setText(null);
			setGraphic(null);
			return;
		}


		if(map.containsKey(status.getId())){
			setGraphic(map.get(status.getId()));
			return;
		}

		JavatterFxmlLoader<TweetObjectController> loader=new JavatterFxmlLoader<>();
		@SuppressWarnings("unused")
		Parent p=null;
		try {
			p = loader.loadFxml("TweetObject.fxml");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TweetObjectController c=loader.getController();
		try {
			if(!status.isRetweet()){
				c.setAccountName("@"+status.getUser().getScreenName());
				c.setUserName(status.getUser().getName());
				c.setVia(status.getSource());
				c.setTweet(status.getText());
				c.setStatus(status);
				c.setImage(status.getUser().getProfileImageURL());
			}else{
				c.setAccountName("@"+status.getRetweetedStatus().getUser().getScreenName());
				c.setUserName(status.getRetweetedStatus().getUser().getName());
				c.setVia(status.getRetweetedStatus().getSource());
				c.setTweet(status.getRetweetedStatus().getText());
				c.setStatus(status);
				c.setImage(status.getRetweetedStatus().getUser().getProfileImageURL());
				c.setMinImage(status.getUser().getProfileImageURL());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		c.getRootPane().maxWidthProperty().bind(getListView().widthProperty());
		c.getRootPane().prefWidthProperty().bind(getListView().widthProperty());
		Group group = GroupBuilder.create().children(c.getRootPane()).build();
		setGraphic(group);
		map.put(status.getId(), group);
	}
}
