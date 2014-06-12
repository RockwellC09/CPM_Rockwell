//
//  ItemViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ItemViewController : UIViewController
{
    IBOutlet UILabel *titleLabel;
}

@property (strong, nonatomic) NSString *title;
@end
