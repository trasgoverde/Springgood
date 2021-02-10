import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAppuser, Appuser } from 'app/shared/model/appuser.model';
import { AppuserService } from './appuser.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';
import { IBlog } from 'app/shared/model/blog.model';
import { BlogService } from 'app/entities/blog/blog.service';
import { INotification } from 'app/shared/model/notification.model';
import { NotificationService } from 'app/entities/notification/notification.service';
import { IAlbum } from 'app/shared/model/album.model';
import { AlbumService } from 'app/entities/album/album.service';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment/comment.service';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';
import { ICmessage } from 'app/shared/model/cmessage.model';
import { CmessageService } from 'app/entities/cmessage/cmessage.service';
import { IFollow } from 'app/shared/model/follow.model';
import { FollowService } from 'app/entities/follow/follow.service';
import { IBlockuser } from 'app/shared/model/blockuser.model';
import { BlockuserService } from 'app/entities/blockuser/blockuser.service';
import { IVtopic } from 'app/shared/model/vtopic.model';
import { VtopicService } from 'app/entities/vtopic/vtopic.service';
import { IVquestion } from 'app/shared/model/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/vquestion.service';
import { IVanswer } from 'app/shared/model/vanswer.model';
import { VanswerService } from 'app/entities/vanswer/vanswer.service';
import { IVthumb } from 'app/shared/model/vthumb.model';
import { VthumbService } from 'app/entities/vthumb/vthumb.service';
import { IProposal } from 'app/shared/model/proposal.model';
import { ProposalService } from 'app/entities/proposal/proposal.service';
import { IProposalVote } from 'app/shared/model/proposal-vote.model';
import { ProposalVoteService } from 'app/entities/proposal-vote/proposal-vote.service';

type SelectableEntity =
  | ICommunity
  | IBlog
  | INotification
  | IAlbum
  | IComment
  | IPost
  | ICmessage
  | IFollow
  | IBlockuser
  | IVtopic
  | IVquestion
  | IVanswer
  | IVthumb
  | IProposal
  | IProposalVote;

@Component({
  selector: 'jhi-appuser-update',
  templateUrl: './appuser-update.component.html',
})
export class AppuserUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];
  blogs: IBlog[] = [];
  notifications: INotification[] = [];
  albums: IAlbum[] = [];
  comments: IComment[] = [];
  posts: IPost[] = [];
  cmessages: ICmessage[] = [];
  follows: IFollow[] = [];
  blockusers: IBlockuser[] = [];
  vtopics: IVtopic[] = [];
  vquestions: IVquestion[] = [];
  vanswers: IVanswer[] = [];
  vthumbs: IVthumb[] = [];
  proposals: IProposal[] = [];
  proposalvotes: IProposalVote[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    assignedVotesPoints: [],
    community: [],
    blog: [],
    notification: [],
    album: [],
    comment: [],
    post: [],
    sender: [],
    receiver: [],
    followed: [],
    following: [],
    blockeduser: [],
    blockinguser: [],
    vtopic: [],
    vquestion: [],
    vanswer: [],
    vthumb: [],
    proposal: [],
    proposalVote: [],
  });

  constructor(
    protected appuserService: AppuserService,
    protected communityService: CommunityService,
    protected blogService: BlogService,
    protected notificationService: NotificationService,
    protected albumService: AlbumService,
    protected commentService: CommentService,
    protected postService: PostService,
    protected cmessageService: CmessageService,
    protected followService: FollowService,
    protected blockuserService: BlockuserService,
    protected vtopicService: VtopicService,
    protected vquestionService: VquestionService,
    protected vanswerService: VanswerService,
    protected vthumbService: VthumbService,
    protected proposalService: ProposalService,
    protected proposalVoteService: ProposalVoteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appuser }) => {
      if (!appuser.id) {
        const today = moment().startOf('day');
        appuser.creationDate = today;
      }

      this.updateForm(appuser);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));

      this.blogService.query().subscribe((res: HttpResponse<IBlog[]>) => (this.blogs = res.body || []));

      this.notificationService.query().subscribe((res: HttpResponse<INotification[]>) => (this.notifications = res.body || []));

      this.albumService.query().subscribe((res: HttpResponse<IAlbum[]>) => (this.albums = res.body || []));

      this.commentService.query().subscribe((res: HttpResponse<IComment[]>) => (this.comments = res.body || []));

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));

      this.cmessageService.query().subscribe((res: HttpResponse<ICmessage[]>) => (this.cmessages = res.body || []));

      this.followService.query().subscribe((res: HttpResponse<IFollow[]>) => (this.follows = res.body || []));

      this.blockuserService.query().subscribe((res: HttpResponse<IBlockuser[]>) => (this.blockusers = res.body || []));

      this.vtopicService.query().subscribe((res: HttpResponse<IVtopic[]>) => (this.vtopics = res.body || []));

      this.vquestionService.query().subscribe((res: HttpResponse<IVquestion[]>) => (this.vquestions = res.body || []));

      this.vanswerService.query().subscribe((res: HttpResponse<IVanswer[]>) => (this.vanswers = res.body || []));

      this.vthumbService.query().subscribe((res: HttpResponse<IVthumb[]>) => (this.vthumbs = res.body || []));

      this.proposalService.query().subscribe((res: HttpResponse<IProposal[]>) => (this.proposals = res.body || []));

      this.proposalVoteService.query().subscribe((res: HttpResponse<IProposalVote[]>) => (this.proposalvotes = res.body || []));
    });
  }

  updateForm(appuser: IAppuser): void {
    this.editForm.patchValue({
      id: appuser.id,
      creationDate: appuser.creationDate ? appuser.creationDate.format(DATE_TIME_FORMAT) : null,
      assignedVotesPoints: appuser.assignedVotesPoints,
      community: appuser.community,
      blog: appuser.blog,
      notification: appuser.notification,
      album: appuser.album,
      comment: appuser.comment,
      post: appuser.post,
      sender: appuser.sender,
      receiver: appuser.receiver,
      followed: appuser.followed,
      following: appuser.following,
      blockeduser: appuser.blockeduser,
      blockinguser: appuser.blockinguser,
      vtopic: appuser.vtopic,
      vquestion: appuser.vquestion,
      vanswer: appuser.vanswer,
      vthumb: appuser.vthumb,
      proposal: appuser.proposal,
      proposalVote: appuser.proposalVote,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appuser = this.createFromForm();
    if (appuser.id !== undefined) {
      this.subscribeToSaveResponse(this.appuserService.update(appuser));
    } else {
      this.subscribeToSaveResponse(this.appuserService.create(appuser));
    }
  }

  private createFromForm(): IAppuser {
    return {
      ...new Appuser(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      assignedVotesPoints: this.editForm.get(['assignedVotesPoints'])!.value,
      community: this.editForm.get(['community'])!.value,
      blog: this.editForm.get(['blog'])!.value,
      notification: this.editForm.get(['notification'])!.value,
      album: this.editForm.get(['album'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      post: this.editForm.get(['post'])!.value,
      sender: this.editForm.get(['sender'])!.value,
      receiver: this.editForm.get(['receiver'])!.value,
      followed: this.editForm.get(['followed'])!.value,
      following: this.editForm.get(['following'])!.value,
      blockeduser: this.editForm.get(['blockeduser'])!.value,
      blockinguser: this.editForm.get(['blockinguser'])!.value,
      vtopic: this.editForm.get(['vtopic'])!.value,
      vquestion: this.editForm.get(['vquestion'])!.value,
      vanswer: this.editForm.get(['vanswer'])!.value,
      vthumb: this.editForm.get(['vthumb'])!.value,
      proposal: this.editForm.get(['proposal'])!.value,
      proposalVote: this.editForm.get(['proposalVote'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppuser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
