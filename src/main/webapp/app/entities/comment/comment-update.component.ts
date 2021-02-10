import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IComment, Comment } from 'app/shared/model/comment.model';
import { CommentService } from './comment.service';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;
  posts: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    commentText: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(65000)]],
    isOffensive: [],
    post: [null, Validators.required],
  });

  constructor(
    protected commentService: CommentService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      if (!comment.id) {
        const today = moment().startOf('day');
        comment.creationDate = today;
      }

      this.updateForm(comment);

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));
    });
  }

  updateForm(comment: IComment): void {
    this.editForm.patchValue({
      id: comment.id,
      creationDate: comment.creationDate ? comment.creationDate.format(DATE_TIME_FORMAT) : null,
      commentText: comment.commentText,
      isOffensive: comment.isOffensive,
      post: comment.post,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.createFromForm();
    if (comment.id !== undefined) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  private createFromForm(): IComment {
    return {
      ...new Comment(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      commentText: this.editForm.get(['commentText'])!.value,
      isOffensive: this.editForm.get(['isOffensive'])!.value,
      post: this.editForm.get(['post'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
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

  trackById(index: number, item: IPost): any {
    return item.id;
  }
}
