/*
 * Copyright (c) 2015 Vimeo (https://vimeo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.vimeo.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vimeo.networking.Vimeo;
import com.vimeo.networking.model.Privacy.PrivacyValue;
import com.vimeo.networking.model.UserBadge.UserBadgeType;
import com.vimeo.networking.model.notifications.NotificationConnection;
import com.vimeo.stag.UseStag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alfredhanssen on 4/12/15.
 */
@SuppressWarnings("unused")
@UseStag
public class User implements Serializable, Followable {

    private static final long serialVersionUID = -4112910222188194647L;
    private static final String ACCOUNT_BASIC = "basic";
    private static final String ACCOUNT_BUSINESS = "business";
    private static final String ACCOUNT_PLUS = "plus";
    private static final String ACCOUNT_PRO = "pro";
    private static final String ACCOUNT_STAFF = "staff";

    public enum AccountType {
        BASIC,
        BUSINESS,
        PRO,
        PLUS,
        STAFF
    }

    @SerializedName("uri")
    private String mUri;

    @SerializedName("name")
    private String mName;

    @SerializedName("link")
    private String mLink;

    @SerializedName("location")
    private String mLocation;

    @SerializedName("bio")
    private String mBio;

    @SerializedName("created_time")
    private Date mCreatedTime;

    @SerializedName("account")
    private String mAccount;

    @SerializedName("pictures")
    private PictureCollection mPictures;

    @SerializedName("emails")
    private ArrayList<Email> mEmails;

    @SerializedName("websites")
    private ArrayList<Website> mWebsites;

    @SerializedName("metadata")
    private Metadata mMetadata;

    @SerializedName("upload_quota")
    private UploadQuota mUploadQuota;

    @Nullable
    @SerializedName("preferences")
    private Preferences mPreferences;
    @Nullable
    @SerializedName("badge")
    private UserBadge mBadge;

    public AccountType getAccountType() {
        if (this.mAccount == null) {
            //We should assume the account object could be null; also, a User object could be created with
            // just a uri, then updated when fetched from the server, so account would be null until then.
            // Scenario: deeplinking. [KZ] 9/29/15
            return AccountType.BASIC;
        }
        switch (this.mAccount) {
            case ACCOUNT_BUSINESS:
                return AccountType.BUSINESS;
            case ACCOUNT_PLUS:
                return AccountType.PLUS;
            case ACCOUNT_PRO:
                return AccountType.PRO;
            case ACCOUNT_STAFF:
                return AccountType.STAFF;
            case ACCOUNT_BASIC:
            default:
                return AccountType.BASIC;
        }
    }

    public UserBadgeType getUserBadgeType() {
        return mBadge == null ? UserBadgeType.NONE : mBadge.getBadgeType();
    }

    public PictureCollection getPictures() {
        return mPictures;
    }

    public void setPictures(PictureCollection pictures) {
        mPictures = pictures;
    }

    @Nullable
    private ConnectionCollection getMetadataConnections() {
        if (mMetadata != null) {
            return mMetadata.getConnections();
        }
        return null;
    }

    @Nullable
    private InteractionCollection getMetadataInteractions() {
        if (mMetadata != null) {
            return mMetadata.getInteractions();
        }
        return null;
    }

    @Nullable
    @Override
    public Interaction getFollowInteraction() {
        InteractionCollection interactions = getMetadataInteractions();
        return interactions != null ? interactions.getFollow() : null;
    }

    @Override
    public boolean canFollow() {
        return getFollowInteraction() != null;
    }

    @Override
    public boolean isFollowing() {
        Interaction follow = getFollowInteraction();
        return follow != null && follow.isAdded();
    }

    @Nullable
    public Connection getFollowingConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getFollowing() : null;
    }

    @Nullable
    public Connection getFeedConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getFeed() : null;
    }

    @Nullable
    public Connection getFollowersConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getFollowers() : null;
    }

    public int getFollowerCount() {
        Connection followers = getFollowersConnection();
        return followers != null ? followers.getTotal() : 0;
    }

    public int getFollowingCount() {
        Connection following = getFollowingConnection();
        return following != null ? following.getTotal() : 0;
    }

    @Nullable
    public Connection getLikesConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getLikes() : null;
    }

    public int getLikesCount() {
        Connection likes = getLikesConnection();
        return likes != null ? likes.getTotal() : 0;
    }

    @Nullable
    public Connection getFollowedChannelsConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getChannels() : null;
    }

    public int getChannelsCount() {
        Connection channels = getFollowedChannelsConnection();
        return channels != null ? channels.getTotal() : 0;
    }

    @Nullable
    public Connection getModeratedChannelsConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getModeratedChannels() : null;
    }

    public int getModeratedChannelsConnectionCount() {
        Connection moderatedChannels = getModeratedChannelsConnection();
        return moderatedChannels != null ? moderatedChannels.getTotal() : 0;
    }

    @Nullable
    public Connection getAppearancesConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getAppearances() : null;
    }

    public int getAppearancesCount() {
        Connection appearances = getAppearancesConnection();
        return appearances != null ? appearances.getTotal() : 0;
    }

    @Nullable
    public Connection getWatchLaterConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getWatchlater() : null;
    }

    @Nullable
    public Connection getWatchedVideosConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getWatchedVideos() : null;
    }

    @Nullable
    public NotificationConnection getNotificationConnection() {
        ConnectionCollection collections = getMetadataConnections();
        return collections != null ? collections.getNotifications() : null;
    }

    @NotNull
    public ArrayList<Picture> getPicturesList() {
        if (mPictures == null || mPictures.getPictures() == null) {
            return new ArrayList<>();
        }
        return mPictures.getPictures();
    }

    @Nullable
    public Connection getVideosConnection() {
        ConnectionCollection connections = getMetadataConnections();
        return connections != null ? connections.getVideos() : null;
    }

    public int getVideoCount() {
        Connection videos = getVideosConnection();
        return videos != null ? videos.getTotal() : 0;
    }

    public boolean isPlusOrPro() {
        boolean plusOrPro = false;
        if (((getAccountType() == AccountType.PLUS) || (getAccountType() == AccountType.PRO))) {
            plusOrPro = true;
        }
        return plusOrPro;
    }

    @Nullable
    public PrivacyValue getPreferredVideoPrivacyValue() {
        PrivacyValue privacyValue = null;
        if (mPreferences != null && mPreferences.getVideos() != null &&
            mPreferences.getVideos().getPrivacy() != null) {
            privacyValue = mPreferences.getVideos().getPrivacy().getView();
        }
        return privacyValue;
    }

    public boolean canUploadPicture() {
        return mMetadata != null &&
               mMetadata.getConnections() != null &&
               mMetadata.getConnections().getPictures() != null &&
               mMetadata.getConnections().getPictures().getOptions() != null &&
               mMetadata.getConnections().getPictures().getOptions().contains(Vimeo.OPTIONS_POST);
    }

    public UploadQuota getUploadQuota() {
        return mUploadQuota;
    }

    public void setUploadQuota(UploadQuota uploadQuota) {
        mUploadQuota = uploadQuota;
    }

    // Returns -1 if there is no space object on this user
    public long getFreeUploadSpace() {
        if (mUploadQuota != null) {
            return mUploadQuota.getFreeUploadSpace();
        }
        return Vimeo.NOT_FOUND;
    }

    /**
     * -----------------------------------------------------------------------------------------------------
     * Getters and Setters
     * -----------------------------------------------------------------------------------------------------
     */
    // <editor-fold desc="Getters">
    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public Date getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(Date createdTime) {
        mCreatedTime = createdTime;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public ArrayList<Email> getVerifiedEmails() {
        return mEmails;
    }

    public ArrayList<Website> getWebsites() {
        return mWebsites;
    }

    public void setWebsites(ArrayList<Website> websites) {
        mWebsites = websites;
    }

    public Metadata getMetadata() {
        return mMetadata;
    }

    public void setMetadata(Metadata metadata) {
        mMetadata = metadata;
    }

    @Nullable
    public Preferences getPreferences() {
        return mPreferences;
    }

    public void setPreferences(@Nullable Preferences preferences) {
        mPreferences = preferences;
    }

    public ArrayList<Email> getEmails() {
        return mEmails;
    }

    public void setEmails(ArrayList<Email> emails) {
        mEmails = emails;
    }

    @Nullable
    public UserBadge getBadge() {
        return mBadge;
    }

    public void setBadge(@Nullable UserBadge badge) {
        mBadge = badge;
    }

    // </editor-fold>

    @Override
    public int hashCode() {
        return this.mUri != null ? this.mUri.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User that = (User) o;

        return ((this.mUri != null && that.mUri != null) && this.mUri.equals(that.mUri));
    }
}
