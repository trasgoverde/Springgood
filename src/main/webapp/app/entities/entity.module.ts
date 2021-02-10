import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'blog',
        loadChildren: () => import('./blog/blog.module').then(m => m.SpringgoodBlogModule),
      },
      {
        path: 'post',
        loadChildren: () => import('./post/post.module').then(m => m.SpringgoodPostModule),
      },
      {
        path: 'topic',
        loadChildren: () => import('./topic/topic.module').then(m => m.SpringgoodTopicModule),
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.SpringgoodTagModule),
      },
      {
        path: 'comment',
        loadChildren: () => import('./comment/comment.module').then(m => m.SpringgoodCommentModule),
      },
      {
        path: 'cmessage',
        loadChildren: () => import('./cmessage/cmessage.module').then(m => m.SpringgoodCmessageModule),
      },
      {
        path: 'bmessage',
        loadChildren: () => import('./bmessage/bmessage.module').then(m => m.SpringgoodBmessageModule),
      },
      {
        path: 'notification',
        loadChildren: () => import('./notification/notification.module').then(m => m.SpringgoodNotificationModule),
      },
      {
        path: 'appphoto',
        loadChildren: () => import('./appphoto/appphoto.module').then(m => m.SpringgoodAppphotoModule),
      },
      {
        path: 'appprofile',
        loadChildren: () => import('./appprofile/appprofile.module').then(m => m.SpringgoodAppprofileModule),
      },
      {
        path: 'community',
        loadChildren: () => import('./community/community.module').then(m => m.SpringgoodCommunityModule),
      },
      {
        path: 'follow',
        loadChildren: () => import('./follow/follow.module').then(m => m.SpringgoodFollowModule),
      },
      {
        path: 'blockuser',
        loadChildren: () => import('./blockuser/blockuser.module').then(m => m.SpringgoodBlockuserModule),
      },
      {
        path: 'album',
        loadChildren: () => import('./album/album.module').then(m => m.SpringgoodAlbumModule),
      },
      {
        path: 'calbum',
        loadChildren: () => import('./calbum/calbum.module').then(m => m.SpringgoodCalbumModule),
      },
      {
        path: 'photo',
        loadChildren: () => import('./photo/photo.module').then(m => m.SpringgoodPhotoModule),
      },
      {
        path: 'interest',
        loadChildren: () => import('./interest/interest.module').then(m => m.SpringgoodInterestModule),
      },
      {
        path: 'activity',
        loadChildren: () => import('./activity/activity.module').then(m => m.SpringgoodActivityModule),
      },
      {
        path: 'celeb',
        loadChildren: () => import('./celeb/celeb.module').then(m => m.SpringgoodCelebModule),
      },
      {
        path: 'cinterest',
        loadChildren: () => import('./cinterest/cinterest.module').then(m => m.SpringgoodCinterestModule),
      },
      {
        path: 'cactivity',
        loadChildren: () => import('./cactivity/cactivity.module').then(m => m.SpringgoodCactivityModule),
      },
      {
        path: 'cceleb',
        loadChildren: () => import('./cceleb/cceleb.module').then(m => m.SpringgoodCcelebModule),
      },
      {
        path: 'vtopic',
        loadChildren: () => import('./vtopic/vtopic.module').then(m => m.SpringgoodVtopicModule),
      },
      {
        path: 'vquestion',
        loadChildren: () => import('./vquestion/vquestion.module').then(m => m.SpringgoodVquestionModule),
      },
      {
        path: 'vanswer',
        loadChildren: () => import('./vanswer/vanswer.module').then(m => m.SpringgoodVanswerModule),
      },
      {
        path: 'vthumb',
        loadChildren: () => import('./vthumb/vthumb.module').then(m => m.SpringgoodVthumbModule),
      },
      {
        path: 'proposal',
        loadChildren: () => import('./proposal/proposal.module').then(m => m.SpringgoodProposalModule),
      },
      {
        path: 'proposal-vote',
        loadChildren: () => import('./proposal-vote/proposal-vote.module').then(m => m.SpringgoodProposalVoteModule),
      },
      {
        path: 'appuser',
        loadChildren: () => import('./appuser/appuser.module').then(m => m.SpringgoodAppuserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class SpringgoodEntityModule {}
