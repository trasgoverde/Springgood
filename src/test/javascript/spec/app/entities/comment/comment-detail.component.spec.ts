import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { CommentDetailComponent } from 'app/entities/comment/comment-detail.component';
import { Comment } from 'app/shared/model/comment.model';

describe('Component Tests', () => {
  describe('Comment Management Detail Component', () => {
    let comp: CommentDetailComponent;
    let fixture: ComponentFixture<CommentDetailComponent>;
    const route = ({ data: of({ comment: new Comment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [CommentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CommentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load comment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.comment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
