import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IPost, Post } from 'app/shared/model/post.model';
import { PostService } from './post.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IBlog } from 'app/shared/model/blog.model';
import { BlogService } from 'app/entities/blog/blog.service';

@Component({
  selector: 'jhi-post-update',
  templateUrl: './post-update.component.html',
})
export class PostUpdateComponent implements OnInit {
  isSaving = false;
  blogs: IBlog[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    publicationDate: [],
    headline: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    leadtext: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    bodytext: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(65000)]],
    quote: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    conclusion: [null, [Validators.minLength(2), Validators.maxLength(2000)]],
    linkText: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    linkURL: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    image: [],
    imageContentType: [],
    blog: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected postService: PostService,
    protected blogService: BlogService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ post }) => {
      if (!post.id) {
        const today = moment().startOf('day');
        post.creationDate = today;
        post.publicationDate = today;
      }

      this.updateForm(post);

      this.blogService.query().subscribe((res: HttpResponse<IBlog[]>) => (this.blogs = res.body || []));
    });
  }

  updateForm(post: IPost): void {
    this.editForm.patchValue({
      id: post.id,
      creationDate: post.creationDate ? post.creationDate.format(DATE_TIME_FORMAT) : null,
      publicationDate: post.publicationDate ? post.publicationDate.format(DATE_TIME_FORMAT) : null,
      headline: post.headline,
      leadtext: post.leadtext,
      bodytext: post.bodytext,
      quote: post.quote,
      conclusion: post.conclusion,
      linkText: post.linkText,
      linkURL: post.linkURL,
      image: post.image,
      imageContentType: post.imageContentType,
      blog: post.blog,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('springgoodApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const post = this.createFromForm();
    if (post.id !== undefined) {
      this.subscribeToSaveResponse(this.postService.update(post));
    } else {
      this.subscribeToSaveResponse(this.postService.create(post));
    }
  }

  private createFromForm(): IPost {
    return {
      ...new Post(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      publicationDate: this.editForm.get(['publicationDate'])!.value
        ? moment(this.editForm.get(['publicationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      headline: this.editForm.get(['headline'])!.value,
      leadtext: this.editForm.get(['leadtext'])!.value,
      bodytext: this.editForm.get(['bodytext'])!.value,
      quote: this.editForm.get(['quote'])!.value,
      conclusion: this.editForm.get(['conclusion'])!.value,
      linkText: this.editForm.get(['linkText'])!.value,
      linkURL: this.editForm.get(['linkURL'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      blog: this.editForm.get(['blog'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPost>>): void {
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

  trackById(index: number, item: IBlog): any {
    return item.id;
  }
}
